package com.misenpai.shared.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.misenpai.anivault.data.db.AppDatabase
import com.misenpai.anivault.data.network.JikanApiService
import com.misenpai.anivault.data.network.MyApi
import com.misenpai.anivault.data.network.response.AnimeDetails
import com.misenpai.anivault.data.network.response.AnimeStatusData
import com.misenpai.anivault.data.network.response.AnimeStatusUpdateData
import com.misenpai.anivault.data.repository.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CompletedViewModel(
    private val api: MyApi,
    private val db: AppDatabase,
    private val jikanApiService: JikanApiService,
    private val repository: UserRepository
) : ViewModel() {

    private val _animeList = MutableLiveData<List<AnimeStatusDataWithDetailsCompleted>>()
    val animeList: LiveData<List<AnimeStatusDataWithDetailsCompleted>> = _animeList

    private val _updateResult = MutableLiveData<ResultCompleted<String>>()
    val updateResult: LiveData<ResultCompleted<String>> = _updateResult

    private val _currentUserId = MutableLiveData<Int>()
    val currentUserId: LiveData<Int> = _currentUserId

    private val _deleteResult = MutableLiveData<ResultCompleted<String>>()
    val deleteResult: LiveData<ResultCompleted<String>> = _deleteResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    init {
        viewModelScope.launch {
            db.getUserDao().getAnyUser().observeForever { user ->
                user?.let {
                    _currentUserId.value = it.id!!
                }
            }
        }
    }

    fun updateWatchedEpisodes(anime: AnimeStatusDataWithDetailsCompleted, userId: Int) {
        viewModelScope.launch {
            try {
                val currentEpisodes = anime.statusData.total_watched_episodes
                val totalEpisodes = anime.statusData.total_episodes
                Log.d("UpdateAnime", "UpdateData: $userId")

                if (currentEpisodes < totalEpisodes) {
                    val updatedEpisodes = currentEpisodes + 1
                    val updateData = AnimeStatusUpdateData(
                        status = "Watching",
                        mal_id = anime.statusData.mal_id,
                        user_id = userId,
                        total_watched_episodes = updatedEpisodes
                    )

                    Log.d("UpdateAnime", "UpdateData: $updateData")

                    val response = repository.updateAnimeStatus(updateData)
                    Log.d("UpdateAnime", "UpdateData: {${response.body()}}")
                    if (response.isSuccessful) {
                        _updateResult.value = ResultCompleted.Success(response.body()?.message ?: "Update successful")
                        Log.d("UpdateAnime", "Update successful")
                        loadPlanToWatchAnime()  // Refresh the list after successful update
                    } else {
                        val errorMessage = response.errorBody()?.string()
                        _updateResult.value = ResultCompleted.Error(Exception("Update failed: $errorMessage"))
                        Log.e("UpdateAnime", "Update failed: $errorMessage")
                    }
                } else {
                    _updateResult.value = ResultCompleted.Error(Exception("Cannot update: Already completed"))
                    Log.e("UpdateAnime", "Cannot update: Already completed")
                }
            } catch (e: Exception) {
                _updateResult.value = ResultCompleted.Error(e)
                Log.e("UpdateAnime", "Exception during update", e)
            }
        }
    }

    fun deleteAnimeStatus(animeId: Int, userId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.removeAnimeStatus(userId, animeId)
                if (response.isSuccessful) {
                    _deleteResult.value = ResultCompleted.Success(response.body()?.message ?: "Delete successful")
                    loadPlanToWatchAnime()  // Refresh the list after successful deletion
                } else {
                    val errorMessage = response.errorBody()?.string()
                    _deleteResult.value = ResultCompleted.Error(Exception("Delete failed: $errorMessage"))
                }
            } catch (e: Exception) {
                _deleteResult.value = ResultCompleted.Error(e)
            }
        }
    }

    fun loadPlanToWatchAnime() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val userLiveData = db.getUserDao().getAnyUser()
                userLiveData.observeForever { user ->
                    user?.let {
                        viewModelScope.launch {
                            try {
                                val response = api.readAnimeStatus(it.id!!, "Completed")
                                if (response.isSuccessful) {
                                    val animeList = response.body()?.animes ?: emptyList()
                                    val animeListWithDetails =
                                        mutableListOf<AnimeStatusDataWithDetailsCompleted>()
                                    animeList.map { anime ->
                                        try {
                                            val details =
                                                jikanApiService.getAnimeDetails(anime.mal_id).data
                                            animeListWithDetails.add(
                                                AnimeStatusDataWithDetailsCompleted(
                                                    anime,
                                                    details
                                                )
                                            )
                                            delay(1000) // Wait for 1 second between requests
                                        } catch (e: Exception) {
                                            Log.e(
                                                "Completed",
                                                "Error fetching details for anime ${anime.mal_id}",
                                                e
                                            )
                                        }
                                    }
                                    _animeList.value = animeListWithDetails
                                }
                            } catch (e: Exception) {
                                Log.e("Completed", "Error fetching anime status", e)
                            } finally {
                                _isLoading.value = false
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("Completed", "Error loading user", e)
                _isLoading.value = false
            }
        }
    }
}

sealed class ResultCompleted<out T> {
    data class Success<out T>(val data: T) : ResultCompleted<T>()
    data class Error(val exception: Exception) : ResultCompleted<Nothing>()
}

data class AnimeStatusDataWithDetailsCompleted(
    val statusData: AnimeStatusData,
    val details: AnimeDetails
)