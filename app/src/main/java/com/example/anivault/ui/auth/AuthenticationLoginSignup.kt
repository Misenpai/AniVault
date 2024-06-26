package com.example.anivault.ui.auth

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.anivault.R
import com.example.anivault.databinding.ActivityAuthenticationLoginSignupBinding

class AuthenticationLoginSignup : AppCompatActivity() {
    private lateinit var binding: ActivityAuthenticationLoginSignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this,R.layout.activity_authentication_login_signup)
        val viewModel = ViewModelProviders.of(this).get(AuthVIewModelAuthentication::class.java)
        binding.viewmodel = viewModel
        }
}