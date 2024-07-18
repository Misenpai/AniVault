package com.example.anivault.ui.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.anivault.data.db.AppDatabase
import com.example.anivault.data.network.MyApi
import com.example.anivault.ui.viewmodel.CompletedViewModel
import com.example.anivault.ui.viewmodel.DroppedViewModel
import com.example.anivault.ui.viewmodel.PlanToWatchViewModel
import com.example.anivault.ui.viewmodel.WatchingViewModel

class LibraryViewModelFactory(
    private val api: MyApi,
    private val db: AppDatabase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlanToWatchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlanToWatchViewModel(api, db) as T
        }
        if (modelClass.isAssignableFrom(CompletedViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CompletedViewModel(api, db) as T
        }
        if (modelClass.isAssignableFrom(DroppedViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DroppedViewModel(api, db) as T
        }
        if (modelClass.isAssignableFrom(WatchingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WatchingViewModel(api, db) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}