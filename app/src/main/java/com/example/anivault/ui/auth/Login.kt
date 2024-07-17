package com.example.anivault.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.anivault.R
import com.example.anivault.data.db.Entities.User
import com.example.anivault.databinding.ActivityLoginBinding
import com.example.anivault.ui.home.MainActivity
import com.example.anivault.utils.hide
import com.example.anivault.utils.show
import com.example.anivault.utils.toast
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class Login : AppCompatActivity(),AuthListener,KodeinAware {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var progressBar: ProgressBar
    override val kodein by kodein()
    private val factory : AuthViewModelFactory by instance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = DataBindingUtil.setContentView(this,R.layout.activity_login)
        val viewModel = ViewModelProviders.of(this,factory).get(AuthViewModel::class.java)
        binding.viewmodel = viewModel
        viewModel.authListener = this
        progressBar = binding.progressBar

        viewModel.getLoggedInUser().observe(this, Observer { user ->
            if(user != null){
                Intent(this, MainActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(it)
                }
            }
        })
    }

    override fun onStarted() {
        progressBar.show()
        toast("Login Started")
    }

    override fun onSuccess(user:User) {
        progressBar.hide()
        toast("${user.name} is Logged in")
        (binding.viewmodel)?.checkStoredUsers()
    }

    override fun onFailure(message: String) {
        progressBar.hide()
        toast(message)
    }
}