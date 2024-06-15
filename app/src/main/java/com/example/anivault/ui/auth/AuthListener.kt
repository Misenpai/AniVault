package com.example.anivault.ui.auth

import com.example.anivault.data.db.Entities.User


interface AuthListener {
    fun onStarted()
    fun onSuccess(user:User)
    fun onFailure(message:String)
}