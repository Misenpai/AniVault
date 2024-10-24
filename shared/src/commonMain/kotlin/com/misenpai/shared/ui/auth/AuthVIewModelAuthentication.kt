package com.misenpai.shared.ui.auth

import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModel

class AuthVIewModelAuthentication:ViewModel() {

    fun onLoginButtonClick(view:View){
        val context = view.context
        val intent = Intent(context, Login::class.java)
        context.startActivity(intent)
    }

    fun onSignupButtonClick(view: View){
        val context = view.context
        val intent = Intent(context, Signup::class.java)
        context.startActivity(intent)
    }
}