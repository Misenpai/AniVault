package com.example.anivault.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anivault.data.repository.AnimeRepository
import com.example.anivault.ui.dataclassess.HorizontalAnime
import kotlinx.coroutines.launch

class SearchAnimeHorizontalViewHolder(private val repository: AnimeRepository) : ViewModel() {
    private val _animeList = MutableLiveData<List<HorizontalAnime>>()
    val animeList: LiveData<List<HorizontalAnime>> = _animeList

    fun fetchTopAnime() {
        viewModelScope.launch {
            repository.getTopAnimeHorizontal().collect { animeList ->
                _animeList.value = animeList
            }
        }
    }
}