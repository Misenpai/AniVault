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

    private val allAnimeThisSeason = mutableListOf<Anime>()

    init {
        fetchCurrentSeasonAnime()
    }

    private fun fetchCurrentSeasonAnime() {
        viewModelScope.launch {
            repository.getAnimeListCurrentSeason()
                .catch { e ->
                    _errorMessage.value = e.message
                }
                .collect { pageAnimeList ->
                    allAnimeThisSeason.addAll(pageAnimeList)
                    _animeListThisSeason.value = allAnimeThisSeason.toList()
                }
        }
    }
}