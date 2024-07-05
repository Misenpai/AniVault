package com.example.anivault.ui.home.seasonlayouts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anivault.ui.dataclassess.Anime
import kotlinx.coroutines.launch

class AnimeViewModel(private val repository: AnimeRepository) : ViewModel() {
    private val _animeList = MutableLiveData<List<Anime>>()
    val animeList: LiveData<List<Anime>> get() = _animeList

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    init {
        fetchCurrentSeasonAnime()
    }

    private fun fetchCurrentSeasonAnime() {
        viewModelScope.launch {
            try {
                val animeList = repository.getAnimeList()
                _animeList.value = animeList
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }
}
