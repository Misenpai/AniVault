package com.misenpai.shared.ui.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.misenpai.anivault.data.repository.UserRepository
import com.misenpai.anivault.ui.viewmodel.AddingAnimeDatabaseViewModel

class AddingAnimeDatabaseViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddingAnimeDatabaseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddingAnimeDatabaseViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}