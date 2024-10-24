package com.misenpai.shared.ui.home.mainactivitylayout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.misenpai.anivault.R
import com.misenpai.anivault.ui.viewmodel.ProfileViewModel

class profile : Fragment() {

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        setupClickListeners(view)
    }

    private fun setupClickListeners(view: View) {
        view.findViewById<ImageView>(R.id.githubImageView).setOnClickListener {
            startActivity(viewModel.openGitHub())
        }
        view.findViewById<ImageView>(R.id.linkedinImageView).setOnClickListener {
            startActivity(viewModel.openLinkedIn())
        }
        view.findViewById<ImageView>(R.id.instagramImageView).setOnClickListener {
            startActivity(viewModel.openInstagram())
        }
    }
}