package com.misenpai.shared.ui.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.misenpai.anivault.data.network.JikanApiService
import com.misenpai.anivault.ui.viewmodel.SearchViewModel

class SearchViewModelFactory(private val jikanApiService: JikanApiService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchViewModel(jikanApiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}