package com.misenpai.shared.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.misenpai.anivault.data.db.Entities.User
import com.misenpai.anivault.data.network.response.AnimeStatusData
import com.misenpai.anivault.data.network.response.AnimeStatusUpdateData
import com.misenpai.anivault.data.repository.UserRepository
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


    fun saveOrUpdateAnimeStatus(data: AnimeStatusData) {
        viewModelScope.launch {
            try {
                Log.d("UpdateAnime", "UpdateData: ${data.user_id}")
                val updateData = AnimeStatusUpdateData(
                    status = data.status,
                    mal_id = data.mal_id,
                    user_id = data.user_id,
                    total_watched_episodes = data.total_watched_episodes
                )

                Log.d("UpdateAnime", "UpdateData: $updateData")
                val response = repository.updateAnimeStatus(updateData)
                if (response.isSuccessful) {
                    _saveResult.value = Result.Success(response.body()?.message ?: "Update successful")
                } else if (response.code() == 404) {
                    // If update fails because the record doesn't exist, try to insert
                    val insertResponse = repository.insertAnimeStatus(data)
                    if (insertResponse.isSuccessful) {
                        _saveResult.value = Result.Success(insertResponse.body()?.message ?: "Insert successful")
                    } else {
                        _saveResult.value = Result.Error(Exception("Failed to insert: ${insertResponse.errorBody()?.string()}"))
                    }
                } else {
                    _saveResult.value = Result.Error(Exception("Update failed: ${response.errorBody()?.string()}"))
                }
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