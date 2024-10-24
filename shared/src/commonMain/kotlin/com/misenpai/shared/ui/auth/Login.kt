package com.misenpai.shared.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.misenpai.anivault.R
import com.misenpai.anivault.data.db.Entities.User
import com.misenpai.anivault.databinding.ActivityLoginBinding
import com.misenpai.anivault.ui.home.MainActivity
import com.misenpai.anivault.utils.toast
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class Login : AppCompatActivity(), AuthListener, KodeinAware {

    private lateinit var binding: ActivityLoginBinding

    override val kodein by kodein()
    private val factory: AuthViewModelFactory by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        val viewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
        viewModel.authListener = this

        viewModel.getLoggedInUser().observe(this, Observer { user ->
            if (user != null) {
                Intent(this, MainActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(it)
                }
            }
        })
    }

    private fun startProgressAnimation() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun stopProgressAnimation() {
        binding.progressBar.visibility = View.INVISIBLE
    }

    override fun onStarted() {
        startProgressAnimation()
        toast("Login Started")
    }

    override fun onSuccess(user: User) {
        stopProgressAnimation()
        toast("${user.name} is Logged in")
        binding.viewmodel?.checkStoredUsers()
    }

    override fun onFailure(message: String) {
        stopProgressAnimation()
        toast(message)
    }
}