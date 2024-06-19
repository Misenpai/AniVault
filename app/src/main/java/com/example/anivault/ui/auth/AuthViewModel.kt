package com.example.anivault.ui.auth

import android.view.View
import androidx.lifecycle.ViewModel
import com.example.anivault.data.repository.UserRepository
import com.example.anivault.utils.ApiException
import com.example.anivault.utils.Coroutines
import com.example.anivault.utils.NoInternetException

class AuthViewModel(
    private val repository: UserRepository
):ViewModel() {
    var email:String? = null
    var password:String? = null
    var authListener:AuthListener? = null


    fun getLoggedInUser() = repository.getUser()

    fun onLoginButtonClick(view:View){
        authListener?.onStarted()
        if (email.isNullOrEmpty()||password.isNullOrEmpty()){
            authListener?.onFailure("Invalid Email or Password")
            return
        }
        Coroutines.main {
            try {
                val authResponse = repository.userLogin(email!!, password!!)
                authResponse.result?.payload?.let {
                    authListener?.onSuccess(it)
                    repository.saveUser(it)
                    return@main
                }
                authListener?.onFailure("Failed to login")
            }catch(e: ApiException){
                authListener?.onFailure(e.message!!)
            }catch(e: NoInternetException){
                authListener?.onFailure(e.message!!)
            }
        }
    }


}