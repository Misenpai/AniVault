package com.example.anivault

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.anivault.ui.auth.AuthViewModel
import com.example.anivault.ui.auth.AuthViewModelFactory
import com.example.anivault.ui.home.MainActivity
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class SplashActivity : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()
    private val factory: AuthViewModelFactory by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("SplashActivity", "onCreate started")

        val viewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)

        viewModel.getLoggedInUser().observe(this, Observer { user ->
            Log.d("SplashActivity", "User observed: ${user?.id}")
            if (user != null) {
                Log.d("SplashActivity", "User is logged in, navigating to MainActivity")
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                Log.d("SplashActivity", "User is not logged in, navigating to GETSTARTED")
                startActivity(Intent(this, GETSTARTED::class.java))
            }
            finish()
        })
    }
}