package com.misenpai.shared.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.misenpai.anivault.data.network.JikanApiService
import com.misenpai.anivault.data.network.response.AnimeSearchItem
import kotlinx.coroutines.launch

class SearchViewModel(private val jikanApiService: JikanApiService) : ViewModel() {
    private val _searchResults = MutableLiveData<List<AnimeSearchItem>>()
    val searchResults: LiveData<List<AnimeSearchItem>> = _searchResults

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun searchAnime(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = jikanApiService.searchAnime(query)
                if (response.isSuccessful) {
                    _searchResults.value = response.body()?.data
                } else {
                    _error.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}