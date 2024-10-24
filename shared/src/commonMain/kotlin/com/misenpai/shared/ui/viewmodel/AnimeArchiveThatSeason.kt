package com.misenpai.shared.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.misenpai.anivault.data.`object`.SharedViewModel
import com.misenpai.anivault.data.repository.AnimeRepository
import com.misenpai.anivault.ui.dataclassess.Anime
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AnimeArchiveThatSeason(private val repository: AnimeRepository) : ViewModel() {
    private val _animeArchiveThatSeason = MutableLiveData<List<Anime>>()
    val animeArchiveThatSeason: LiveData<List<Anime>> get() = _animeArchiveThatSeason
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading


    private val allAnimeArchiveThatSeason = mutableListOf<Anime>()

    init {
        SharedViewModel.selectedSeason.observeForever { (year, season) ->
            fetchPreviousSeasonAnime(year, season)
        }
    }

    fun fetchPreviousSeasonAnime(year: Int, season: String) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getAnimeArchive(year.toString(),season)
                .catch { e ->
                    _errorMessage.value = e.message
                    _isLoading.value = false
                }
                .collect { pageAnimeList ->
                    allAnimeArchiveThatSeason.addAll(pageAnimeList)
                    _animeArchiveThatSeason.value = allAnimeArchiveThatSeason.toList()
                    _isLoading.value = false
                }
        }
    }
}