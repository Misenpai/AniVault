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

    class WatchingViewModel(
        private val api: MyApi,
        private val db: AppDatabase,
        private val jikanApiService: JikanApiService,
        private val repository: UserRepository
    ) : ViewModel() {

        private val _animeList = MutableLiveData<List<AnimeStatusDataWithDetailsWatching>>()
        val animeList: LiveData<List<AnimeStatusDataWithDetailsWatching>> = _animeList

        private val _updateResult = MutableLiveData<ResultWatching<String>>()
        val updateResult: LiveData<ResultWatching<String>> = _updateResult

        private val _currentUserId = MutableLiveData<Int>()
        val currentUserId: LiveData<Int> = _currentUserId

        private val _deleteResult = MutableLiveData<ResultWatching<String>>()
        val deleteResult: LiveData<ResultWatching<String>> = _deleteResult

        init {
            viewModelScope.launch {
                db.getUserDao().getAnyUser().observeForever { user ->
                    user?.let {
                        _currentUserId.value = it.id!!
                    }
                }
            }
        }

        fun updateWatchedEpisodes(anime: AnimeStatusDataWithDetailsWatching, userId: Int) {
            viewModelScope.launch {
                try {
                    val currentEpisodes = anime.statusData.total_watched_episodes
                    val totalEpisodes = anime.statusData.total_episodes
                    Log.d("UpdateAnime", "UpdateData: $userId")

                    if (currentEpisodes < totalEpisodes) {
                        val updatedEpisodes = currentEpisodes + 1
                        val newStatus = if (updatedEpisodes == totalEpisodes) "Completed" else "Currently Watching"
                        val updateData = AnimeStatusUpdateData(
                            status = newStatus,
                            mal_id = anime.statusData.mal_id,
                            user_id = userId,
                            total_watched_episodes = updatedEpisodes
                        )

                        Log.d("UpdateAnime", "UpdateData: $updateData")

                        val response = repository.updateAnimeStatus(updateData)
                        Log.d("UpdateAnime", "UpdateData: {${response.body()}}")
                        if (response.isSuccessful) {
                            _updateResult.value = ResultWatching.Success(response.body()?.message ?: "Update successful")
                            Log.d("UpdateAnime", "Update successful")
                            loadWatchingAnime()  // Refresh the list after successful update
                        } else {
                            val errorMessage = response.errorBody()?.string()
                            _updateResult.value = ResultWatching.Error(Exception("Update failed: $errorMessage"))
                            Log.e("UpdateAnime", "Update failed: $errorMessage")
                        }
                    } else {
                        _updateResult.value = ResultWatching.Error(Exception("Cannot update: Already completed"))
                        Log.e("UpdateAnime", "Cannot update: Already completed")
                    }
                } catch (e: Exception) {
                    _updateResult.value = ResultWatching.Error(e)
                    Log.e("UpdateAnime", "Exception during update", e)
                }
            }
        }

        fun deleteAnimeStatus(animeId: Int, userId: Int) {
            viewModelScope.launch {
                try {
                    val response = repository.removeAnimeStatus(userId, animeId)
                    if (response.isSuccessful) {
                        _deleteResult.value = ResultWatching.Success(response.body()?.message ?: "Delete successful")
                        loadWatchingAnime()  // Refresh the list after successful deletion
                    } else {
                        val errorMessage = response.errorBody()?.string()
                        _deleteResult.value = ResultWatching.Error(Exception("Delete failed: $errorMessage"))
                    }
                } catch (e: Exception) {
                    _deleteResult.value = ResultWatching.Error(e)
                }
            }
        }

        fun loadWatchingAnime() {
            viewModelScope.launch {
                try {
                    val userLiveData = db.getUserDao().getAnyUser()

                    userLiveData.observeForever { user ->
                        user?.let {
                            viewModelScope.launch {
                                try {
                                    val response = api.readAnimeStatus(it.id!!, "Currently Watching")
                                    if (response.isSuccessful) {
                                        val animeList = response.body()?.animes ?: emptyList()
                                        val animeListWithDetails = animeList.map { anime ->
                                            val details = jikanApiService.getAnimeDetails(anime.mal_id).data
                                            AnimeStatusDataWithDetailsWatching(anime, details)
                                        }
                                        _animeList.value = animeListWithDetails
                                    }
                                } catch (e: Exception) {
                                    Log.e("WatchingViewModel", "Error fetching anime status", e)
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e("WatchingViewModel", "Error loading user", e)
                }
            }
        }
    }

    sealed class ResultWatching<out T> {
        data class Success<out T>(val data: T) : ResultWatching<T>()
        data class Error(val exception: Exception) : ResultWatching<Nothing>()
    }

    data class AnimeStatusDataWithDetailsWatching(
        val statusData: AnimeStatusData,
        val details: AnimeDetails
    )