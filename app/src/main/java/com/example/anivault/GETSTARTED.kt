package com.example.anivault

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.anivault.databinding.ActivityGetstartedBinding
import com.example.anivault.ui.auth.AuthViewModelGetStarted

class GETSTARTED : AppCompatActivity() {
    private lateinit var binding:ActivityGetstartedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this,R.layout.activity_getstarted)
        val viewModel = ViewModelProviders.of(this).get(AuthViewModelGetStarted::class.java)
        binding.viewmodel = viewModel
    }

}