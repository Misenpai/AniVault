package com.misenpai.shared.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.misenpai.anivault.data.repository.AnimeRepository
import com.misenpai.anivault.ui.dataclassess.HorizontalAnime
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SearchAnimeHorizontalViewHolder(private val repository: AnimeRepository) : ViewModel() {
    private val _uiState = MutableLiveData<AnimeSearchUiState>()
    val uiState: LiveData<AnimeSearchUiState> = _uiState

    fun fetchAllAnimeData() {
        viewModelScope.launch {
            _uiState.value = AnimeSearchUiState.Loading

            try {
                val topAiring = async { repository.getTopAiringAnime().first() }
                val topAnime = async { repository.getTopAnimeHorizontal().first() }
                val recommended = async { repository.getRecommendationAnimeHorizontal().first() }
                val topUpcoming = async { repository.getAnimeListNextSeasonSearch().first() }

                val result = AnimeSearchUiState.Success(
                    topAiring = topAiring.await(),
                    topAnime = topAnime.await(),
                    recommended = recommended.await(),
                    topUpcoming = topUpcoming.await()
                )
                _uiState.value = result
            } catch (e: Exception) {
                _uiState.value = AnimeSearchUiState.Error("Failed to load anime data: ${e.message}")
            }
        }
    }
}

sealed class AnimeSearchUiState {
    object Loading : AnimeSearchUiState()
    data class Success(
        val topAiring: List<HorizontalAnime>,
        val topAnime: List<HorizontalAnime>,
        val recommended: List<HorizontalAnime>,
        val topUpcoming: List<HorizontalAnime>
    ) : AnimeSearchUiState()
    data class Error(val message: String) : AnimeSearchUiState()
}