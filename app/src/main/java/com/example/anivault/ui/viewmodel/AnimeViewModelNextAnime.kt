package com.example.anivault.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anivault.data.repository.AnimeRepository
import com.example.anivault.ui.dataclassess.Anime
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AnimeViewModelNextAnime(private val repository: AnimeRepository) : ViewModel() {
    private val _animeListNextSeason = MutableLiveData<List<Anime>>()
    val animeListNextSeason: LiveData<List<Anime>> get() = _animeListNextSeason
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading


    private val allAnimeNextSeason = mutableListOf<Anime>()

    init {
        fetchNextSeasonAnime()
    }

    private fun fetchNextSeasonAnime() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getAnimeListNextSeason()
                .catch { e ->
                    _errorMessage.value = e.message
                    _isLoading.value = false
                }
                .collect { pageAnimeList ->
                    allAnimeNextSeason.addAll(pageAnimeList)
                    _animeListNextSeason.value = allAnimeNextSeason.toList()
                    _isLoading.value = false
                }
        }
    }
}