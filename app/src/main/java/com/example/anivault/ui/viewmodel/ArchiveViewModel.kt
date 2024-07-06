package com.example.anivault.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anivault.data.repository.AnimeRepository
import com.example.anivault.ui.dataclassess.ArchiveYearItems
import kotlinx.coroutines.launch

class ArchiveViewModel(private val repository: AnimeRepository) : ViewModel() {
    private val _archiveItems = MutableLiveData<List<ArchiveYearItems>>()
    val archiveItems: LiveData<List<ArchiveYearItems>> = _archiveItems

    init {
        loadArchiveItems()
    }

    private fun loadArchiveItems() {
        viewModelScope.launch {
            try {
                val seasons = repository.getSeasons()
                _archiveItems.value = seasons.map { yearSeason ->
                    ArchiveYearItems(yearSeason.year, yearSeason.seasons)
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun onSeasonButtonClicked(year: Int, season: String) {
        // Handle the button click, e.g., navigate to season details
    }
}