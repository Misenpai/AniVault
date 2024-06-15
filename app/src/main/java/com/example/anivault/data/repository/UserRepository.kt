package com.example.anivault.data.repository

import com.example.anivault.data.network.MyApi
import com.example.anivault.data.network.SafeApiRequest
import com.example.anivault.data.network.response.AuthResponse
import retrofit2.Response

class UserRepository : SafeApiRequest(){
    suspend fun userLogin(email: String,password: String):AuthResponse{
        return apiRequest {
            MyApi().userLogin(email,password)
        }
    }
}