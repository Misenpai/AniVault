package com.example.anivault.ui.auth

import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.anivault.R
import com.example.anivault.data.db.Entities.User
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

    override fun onSuccess(user:User) {
        progressBar.hide()
        toast("${user.name} is Logged in")
    }

    override fun onFailure(message: String) {
        progressBar.hide()
        toast(message)
    }
}