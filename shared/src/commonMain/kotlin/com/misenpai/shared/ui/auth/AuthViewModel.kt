package com.misenpai.shared.ui.auth

import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.misenpai.anivault.data.repository.UserRepository
import com.misenpai.anivault.utils.ApiException
import com.misenpai.anivault.utils.NoInternetException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import toUser

class AuthViewModel(
    private val repository: UserRepository
) : ViewModel() {
    var name: String? = null
    var email: String? = null
    var password: String? = null
    var authListener: AuthListener? = null

    fun getLoggedInUser() = repository.getAnyUser()

    private fun isValidEmail(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches() &&
                (email.endsWith("@gmail.com") ||
                        email.endsWith("@hotmail.com") ||
                        email.endsWith("@yahoo.com") ||
                        email.endsWith("@outlook.com"))
    }

    fun onLoginButtonClick(view: View) {
        authListener?.onStarted()
        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            authListener?.onFailure("Invalid Email or Password")
            return
        }
        if (!isValidEmail(email!!)) {
            authListener?.onFailure("Not a valid email address")
            return
        }
        viewModelScope.launch {
            try {
                val authResponse = repository.userLogin(email!!, password!!)
                authResponse.result?.payload?.let { userPayload ->
                    val user = userPayload.toUser(authResponse.token)
                    repository.saveUser(user)
                    authListener?.onSuccess(user)
                    checkStoredUsers()
                    return@launch
                }
                authListener?.onFailure("Invalid Email or Password")
            } catch (e: ApiException) {
                authListener?.onFailure(e.message!!)
            } catch (e: NoInternetException) {
                authListener?.onFailure(e.message!!)
            }
        }
    }

    fun onSignupButtonClick(view: View) {
        authListener?.onStarted()

        if (name.isNullOrEmpty() || email.isNullOrEmpty() || password.isNullOrEmpty()) {
            authListener?.onFailure("All fields are required")
            return
        }
        if (!isValidEmail(email!!)) {
            authListener?.onFailure("Not a valid email address")
            return
        }
        viewModelScope.launch {
            try {
                val result = repository.userSignup(name!!, email!!, password!!)
                result.fold(
                    onSuccess = { authResponse ->
                        authResponse.result?.payload?.let { userPayload ->
                            val user = userPayload.toUser(authResponse.token)
                            authListener?.onSuccess(user)
                        } ?: run {
                            authListener?.onFailure("Failed to sign up: Invalid response")
                        }
                    },
                    onFailure = { error ->
                        when {
                            error.message?.contains("already exists") == true -> {
                                authListener?.onFailure("A user with this email or name already exists")
                            }
                            else -> {
                                authListener?.onFailure("Failed to sign up: ${error.message}")
                            }
                        }
                    }
                )
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Signup error", e)
                authListener?.onFailure("An error occurred during signup")
            }
        }
    }

    fun checkStoredUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            val users = repository.getAllUsers()
            Log.d("StoredUsers", "Number of users: ${users.size}")
            withContext(Dispatchers.Main) {
                for (user in users) {
                    Log.d("StoredUser", "ID: ${user.id}, Name: ${user.name}, Email: ${user.email}")
                }
            }
        }
    }

    fun clearUserData() {
        viewModelScope.launch {
            repository.clearAllUserData()
        }
    }
}