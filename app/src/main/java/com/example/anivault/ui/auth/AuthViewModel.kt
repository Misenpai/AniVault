package com.example.anivault.ui.auth

import android.view.View
import androidx.lifecycle.ViewModel
import com.example.anivault.data.repository.UserRepository

class AuthViewModel:ViewModel() {
    var email:String? = null
    var password:String? = null
    var authListener:AuthListener? = null

    fun onLoginButtonClick(view:View){
        authListener?.onStarted()
        if (email.isNullOrEmpty()||password.isNullOrEmpty()){
            authListener?.onFailure("Invalid Email or Password")
            return
        }
        val loginResponse = UserRepository().userLogin(email!!,password!!)
        authListener?.onSuccess(loginResponse)
    }
}