package com.example.anivault.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.anivault.R
import com.example.anivault.databinding.ActivityLoginBinding
import com.example.anivault.utils.hide
import com.example.anivault.utils.show
import com.example.anivault.utils.toast

class Login : AppCompatActivity(),AuthListener {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login)
        val viewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)
        binding.viewmodel = viewModel
        viewModel.authListener = this
        progressBar = binding.progressBar
    }

    override fun onStarted() {
        progressBar.show()
        toast("Login Started")
    }

    override fun onSuccess(loginResponse:LiveData<String>) {
        loginResponse.observe(this, Observer {
            progressBar.hide()
            toast(it)
        })
    }

    override fun onFailure(message: String) {
        progressBar.hide()
        toast(message)
    }
}