package com.example.anivault.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anivault.data.repository.AnimeRepository
import com.example.anivault.ui.dataclassess.HorizontalAnime
import kotlinx.coroutines.launch

class SearchAnimeHorizontalViewHolder(private val repository: AnimeRepository) : ViewModel() {
    private val _topAnimeList = MutableLiveData<List<HorizontalAnime>>()
    val topAnimeList: LiveData<List<HorizontalAnime>> = _topAnimeList

    private val _recommendedAnimeList = MutableLiveData<List<HorizontalAnime>>()
    val recommendedAnimeList: LiveData<List<HorizontalAnime>> = _recommendedAnimeList

    private val _topUpcomingAnimeList = MutableLiveData<List<HorizontalAnime>>()
    val topUpcomingAnimeList: LiveData<List<HorizontalAnime>> = _topUpcomingAnimeList

    private val _topAiringAnimeList = MutableLiveData<List<HorizontalAnime>>()
    val topAiringAnimeList: LiveData<List<HorizontalAnime>> = _topAiringAnimeList

    fun fetchTopAnime() {
        viewModelScope.launch {
            repository.getTopAnimeHorizontal().collect { animeList ->
                _topAnimeList.value = animeList
            }
        }
    }

    fun fetchRecommendedAnime(){
        viewModelScope.launch {
            repository.getRecommendationAnimeHorizontal()
                .collect{
                    animeList->
                    _recommendedAnimeList.value = animeList
                }
        }
    }

    fun fetchTopUpcomingAnime(){
        viewModelScope.launch {
            repository.getAnimeListNextSeasonSearch()
                .collect{
                        animeList->
                    _topUpcomingAnimeList.value = animeList
                }
        }
    }

    fun fetchTopAiringAnime(){
        viewModelScope.launch {
            repository.getTopAiringAnime()
                .collect{
                        animeList->
                    _topAiringAnimeList.value = animeList
                }
        }
    }
}