package com.misenpai.shared.ui.viewmodel

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {
    fun openGitHub(): Intent {
        return openUrl("https://github.com/Misenpai")
    }

    fun openLinkedIn(): Intent {
        return openUrl("https://www.linkedin.com/in/sumit-sinha-67754a230/")
    }

    fun openInstagram(): Intent {
        return openUrl("https://www.instagram.com/misenpai_/")
    }

    private fun openUrl(url: String): Intent {
        return Intent(Intent.ACTION_VIEW, Uri.parse(url))
    }
}