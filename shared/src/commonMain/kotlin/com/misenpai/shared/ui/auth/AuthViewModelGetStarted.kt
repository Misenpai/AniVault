package com.misenpai.shared.ui.auth

import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModel

class AuthViewModelGetStarted:ViewModel() {
    fun onGetStartedButtonClick(view: View) {
        val context = view.context
        val intent = Intent(context, AuthenticationLoginSignup::class.java)
        context.startActivity(intent)
    }
}