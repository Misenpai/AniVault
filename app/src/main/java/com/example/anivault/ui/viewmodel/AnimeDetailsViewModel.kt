package com.example.anivault.ui.viewmodel

import RetrofitInstance
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anivault.data.network.response.AnimeDetails
import kotlinx.coroutines.launch

class AnimeDetailsViewModel : ViewModel() {
    private val _animeDetails = MutableLiveData<AnimeDetails>()
    val animeDetails: LiveData<AnimeDetails> = _animeDetails

    fun fetchAnimeDetails(animeId: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getAnimeDetails(animeId)
                _animeDetails.value = response.data
            } catch (e: Exception) {
            }
        }
    }
}