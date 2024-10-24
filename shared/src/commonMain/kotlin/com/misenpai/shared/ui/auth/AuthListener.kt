package com.misenpai.shared.ui.auth

import com.misenpai.anivault.data.db.Entities.User


interface AuthListener {
    fun onStarted()
    fun onSuccess(user:User)
    fun onFailure(message:String)
}