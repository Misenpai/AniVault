package com.example.anivault.ui.auth

import androidx.lifecycle.LiveData
import com.example.anivault.data.db.Entities.User
import com.example.anivault.data.network.response.UserPayload

interface AuthListener {
    fun onStarted()
    fun onSuccess(user:User)
    fun onFailure(message:String)
}