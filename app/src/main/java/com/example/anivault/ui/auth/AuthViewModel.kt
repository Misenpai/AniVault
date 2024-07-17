package com.example.anivault.ui.auth

import UserRepository
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anivault.utils.ApiException
import com.example.anivault.utils.NoInternetException
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

    fun onLoginButtonClick(view: View) {
        authListener?.onStarted()
        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            authListener?.onFailure("Invalid Email or Password")
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
                authListener?.onFailure("Failed to login")
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

        viewModelScope.launch {
            try {
                val authResponse = repository.userSignup(name!!, email!!, password!!)
                authResponse.result?.payload?.let { userPayload ->
                    val user = userPayload.toUser(authResponse.token)
                    repository.saveUser(user)
                    authListener?.onSuccess(user)
                    checkStoredUsers()
                    return@launch
                }
                authListener?.onFailure("Failed to sign up")
            } catch (e: ApiException) {
                authListener?.onFailure(e.message!!)
            } catch (e: NoInternetException) {
                authListener?.onFailure(e.message!!)
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
}