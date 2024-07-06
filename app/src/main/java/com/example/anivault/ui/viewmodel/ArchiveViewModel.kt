package com.example.anivault.ui.viewmodel

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anivault.data.`object`.SharedViewModel
import com.example.anivault.data.repository.AnimeRepository
import com.example.anivault.ui.dataclassess.ArchiveYearItems
import com.example.anivault.ui.home.seasonlayouts.ArchiveSelected
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

    fun onSeasonButtonClicked(fragmentManager: FragmentManager, containerId: Int, year: Int, season: String) {
        SharedViewModel.setSelectedSeason(year, season)
        val fragment = ArchiveSelected().apply {
            arguments = Bundle().apply {
                putInt("year", year)
                putString("season", season)
            }
        }
        fragmentManager.commit {
            replace(containerId, fragment)
            addToBackStack(null)  // Optional: add to back stack to allow back navigation
        }
    }
}
