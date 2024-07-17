package com.example.anivault.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anivault.data.db.Entities.User
import com.example.anivault.data.network.response.AnimeStatusData
import com.example.anivault.data.repository.UserRepository
import kotlinx.coroutines.launch

class AddingAnimeDatabaseViewModel(private val repository: UserRepository) : ViewModel() {

    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User> = _currentUser

    private val _saveResult = MutableLiveData<Result<String>>()
    val saveResult: LiveData<Result<String>> = _saveResult

    init {
        viewModelScope.launch {
            repository.getCurrentUser().observeForever { user ->
                _currentUser.value = user
            }
        }
    }

    fun getCurrentUserId(): Int? {
        return currentUser.value?.id
    }

    fun saveAnimeStatus(data: AnimeStatusData) {
        viewModelScope.launch {
            try {
                val response = repository.insertAnimeStatus(data)
                _saveResult.value = Result.Success(response.message)
            } catch (e: Exception) {
                _saveResult.value = Result.Error(e)
            }
        }
    }
}

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}