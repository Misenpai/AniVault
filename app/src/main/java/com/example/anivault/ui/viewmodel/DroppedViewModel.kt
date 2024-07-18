package com.example.anivault.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anivault.data.db.AppDatabase
import com.example.anivault.data.network.MyApi
import com.example.anivault.data.network.response.AnimeStatusData
import kotlinx.coroutines.launch

class DroppedViewModel(
    private val api: MyApi,
    private val db: AppDatabase
) : ViewModel() {

    private val _animeList = MutableLiveData<List<AnimeStatusData>>()
    val animeList: LiveData<List<AnimeStatusData>> = _animeList

    fun loadPlanToWatchAnime() {
        viewModelScope.launch {
            try {
                val userLiveData = db.getUserDao().getAnyUser()

                userLiveData.observeForever { user ->
                    Log.d("PlanToWatch", "User: $user")

                    user?.let {
                        viewModelScope.launch {
                            try {
                                val response = api.readAnimeStatus(it.id!!, "Dropped")
                                Log.e("Dropped", "{${response.body()}")
                                if (response.isSuccessful) {
                                    _animeList.value = response.body()?.animes ?: emptyList()
                                }
                            } catch (e: Exception) {
                                Log.e("Dropped", "Error fetching anime status", e)
                                // Handle error
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("Dropped", "Error loading user", e)
                // Handle error
            }
        }
    }
}