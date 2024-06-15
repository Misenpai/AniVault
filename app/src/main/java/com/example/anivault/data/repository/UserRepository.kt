package com.example.anivault.data.repository

import com.example.anivault.data.network.MyApi
import com.example.anivault.data.network.response.AuthResponse
import retrofit2.Response

class UserRepository {
    suspend fun userLogin(email: String,password: String):Response<AuthResponse>{
        return MyApi().userLogin(email,password)
    }
}