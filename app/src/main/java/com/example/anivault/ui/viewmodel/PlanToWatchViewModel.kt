package com.example.anivault.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anivault.data.db.AppDatabase
import com.example.anivault.data.network.JikanApiService
import com.example.anivault.data.network.MyApi
import com.example.anivault.data.network.response.AnimeDetails
import com.example.anivault.data.network.response.AnimeStatusData
import com.example.anivault.data.network.response.AnimeStatusUpdateData
import com.example.anivault.data.repository.UserRepository
import kotlinx.coroutines.launch

class PlanToWatchViewModel(
    private val api: MyApi,
    private val db: AppDatabase,
    private val jikanApiService: JikanApiService,
    private val repository: UserRepository
) : ViewModel() {

    private val _animeList = MutableLiveData<List<AnimeStatusDataWithDetails>>()
    val animeList: LiveData<List<AnimeStatusDataWithDetails>> = _animeList

    private val _updateResult = MutableLiveData<ResultPlanToWatch<String>>()
    val updateResult: LiveData<ResultPlanToWatch<String>> = _updateResult

    private val _currentUserId = MutableLiveData<Int>()
    val currentUserId: LiveData<Int> = _currentUserId

    private val _deleteResult = MutableLiveData<ResultPlanToWatch<String>>()
    val deleteResult: LiveData<ResultPlanToWatch<String>> = _deleteResult

    init {
        viewModelScope.launch {
            db.getUserDao().getAnyUser().observeForever { user ->
                user?.let {
                    _currentUserId.value = it.id!!
                }
            }
        }
    }

    fun updateWatchedEpisodes(anime: AnimeStatusDataWithDetails, userId: Int) {
        viewModelScope.launch {
            try {
                val currentEpisodes = anime.statusData.total_watched_episodes
                val totalEpisodes = anime.statusData.total_episodes
                Log.d("UpdateAnime", "UpdateData: $userId")

                if (currentEpisodes < totalEpisodes) {
                    val updatedEpisodes = currentEpisodes + 1
                    val updateData = AnimeStatusUpdateData(
                        status = "Currently Watching",
                        mal_id = anime.statusData.mal_id,
                        user_id = userId,
                        total_watched_episodes = updatedEpisodes
                    )

                    Log.d("UpdateAnime", "UpdateData: $updateData")

                    val response = repository.updateAnimeStatus(updateData)
                    Log.d("UpdateAnime", "UpdateData: {${response.body()}}")
                    if (response.isSuccessful) {
                        _updateResult.value = ResultPlanToWatch.Success(response.body()?.message ?: "Update successful")
                        Log.d("UpdateAnime", "Update successful")
                        loadPlanToWatchAnime()  // Refresh the list after successful update
                    } else {
                        val errorMessage = response.errorBody()?.string()
                        _updateResult.value = ResultPlanToWatch.Error(Exception("Update failed: $errorMessage"))
                        Log.e("UpdateAnime", "Update failed: $errorMessage")
                    }
                } else {
                    _updateResult.value = ResultPlanToWatch.Error(Exception("Cannot update: Already completed"))
                    Log.e("UpdateAnime", "Cannot update: Already completed")
                }
            } catch (e: Exception) {
                _updateResult.value = ResultPlanToWatch.Error(e)
                Log.e("UpdateAnime", "Exception during update", e)
            }
        }
    }


    fun deleteAnimeStatus(animeId: Int, userId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.removeAnimeStatus(userId, animeId)
                if (response.isSuccessful) {
                    _deleteResult.value = ResultPlanToWatch.Success(response.body()?.message ?: "Delete successful")
                    loadPlanToWatchAnime()  // Refresh the list after successful deletion
                } else {
                    val errorMessage = response.errorBody()?.string()
                    _deleteResult.value = ResultPlanToWatch.Error(Exception("Delete failed: $errorMessage"))
                }
            } catch (e: Exception) {
                _deleteResult.value = ResultPlanToWatch.Error(e)
            }
        }
    }


    fun loadPlanToWatchAnime() {
        viewModelScope.launch {
            try {
                val userLiveData = db.getUserDao().getAnyUser()

                userLiveData.observeForever { user ->
                    user?.let {
                        viewModelScope.launch {
                            try {
                                val response = api.readAnimeStatus(it.id!!, "Plan to Watch")
                                if (response.isSuccessful) {
                                    val animeList = response.body()?.animes ?: emptyList()
                                    val animeListWithDetails = animeList.map { anime ->
                                        val details = jikanApiService.getAnimeDetails(anime.mal_id).data
                                        AnimeStatusDataWithDetails(anime, details)
                                    }
                                    _animeList.value = animeListWithDetails
                                }
                            } catch (e: Exception) {
                                Log.e("PlanToWatch", "Error fetching anime status", e)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("PlanToWatch", "Error loading user", e)
            }
        }
    }
}

sealed class ResultPlanToWatch<out T> {
    data class Success<out T>(val data: T) : ResultPlanToWatch<T>()
    data class Error(val exception: Exception) : ResultPlanToWatch<Nothing>()
}


data class AnimeStatusDataWithDetails(
    val statusData: AnimeStatusData,
    val details: AnimeDetails
)