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
import kotlinx.coroutines.launch

class CompletedViewModel(
    private val api: MyApi,
    private val db: AppDatabase,
    private val jikanApiService: JikanApiService
) : ViewModel() {

    private val _animeList = MutableLiveData<List<AnimeStatusDataWithDetailsCompleted>>()
    val animeList: LiveData<List<AnimeStatusDataWithDetailsCompleted>> = _animeList

    fun loadPlanToWatchAnime() {
        viewModelScope.launch {
            try {
                val userLiveData = db.getUserDao().getAnyUser()

                userLiveData.observeForever { user ->
                    user?.let {
                        viewModelScope.launch {
                            try {
                                val response = api.readAnimeStatus(it.id!!, "Completed")
                                if (response.isSuccessful) {
                                    val animeList = response.body()?.animes ?: emptyList()
                                    val animeListWithDetails = animeList.map { anime ->
                                        val details = jikanApiService.getAnimeDetails(anime.mal_id).data
                                        AnimeStatusDataWithDetailsCompleted(anime, details)
                                    }
                                    _animeList.value = animeListWithDetails
                                }
                            } catch (e: Exception) {
                                Log.e("Completed", "Error fetching anime status", e)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("Completed", "Error loading user", e)
            }
        }
    }
}

data class AnimeStatusDataWithDetailsCompleted(
    val statusData: AnimeStatusData,
    val details: AnimeDetails
)