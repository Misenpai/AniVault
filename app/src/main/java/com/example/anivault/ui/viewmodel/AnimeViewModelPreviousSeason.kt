package com.example.anivault.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anivault.data.repository.AnimeRepository
import com.example.anivault.ui.dataclassess.Anime
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AnimeViewModelPreviousSeason(private val repository: AnimeRepository) : ViewModel() {
    private val _animeListPreviousSeason = MutableLiveData<List<Anime>>()
    val animeListPreviousSeason: LiveData<List<Anime>> get() = _animeListPreviousSeason
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage


    private val allAnimePreviousSeason = mutableListOf<Anime>()

    init {

        fetchPreviousSeasonAnime()
    }

    private fun fetchPreviousSeasonAnime() {
        viewModelScope.launch {
            repository.getAnimeListPreviousSeason()
                .catch { e ->
                    _errorMessage.value = e.message
                }
                .collect { pageAnimeList ->
                    allAnimePreviousSeason.addAll(pageAnimeList)
                    _animeListPreviousSeason.value = allAnimePreviousSeason.toList()
                }
        }
    }
}