package com.example.anivault.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anivault.data.repository.AnimeRepository
import com.example.anivault.ui.dataclassess.Anime
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AnimeViewModel(private val repository: AnimeRepository) : ViewModel() {
    private val _animeListThisSeason = MutableLiveData<List<Anime>>()
    val animeListThisSeason: LiveData<List<Anime>> get() = _animeListThisSeason

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val allAnimeThisSeason = mutableListOf<Anime>()

    init {
        fetchCurrentSeasonAnime()
    }

    fun fetchCurrentSeasonAnime() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getAnimeListCurrentSeason()
                .catch { e ->
                    _errorMessage.value = e.message
                    _isLoading.value = false
                }
                .collect { pageAnimeList ->
                    allAnimeThisSeason.addAll(pageAnimeList)
                    _animeListThisSeason.value = allAnimeThisSeason.toList()
                    _isLoading.value = false
                }
        }
    }
}