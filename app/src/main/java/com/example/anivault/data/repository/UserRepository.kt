package com.example.anivault.data.repository

import com.example.anivault.data.db.AppDatabase
import com.example.anivault.data.db.Entities.User
import com.example.anivault.data.network.MyApi
import com.example.anivault.data.network.SafeApiRequest
import com.example.anivault.data.network.response.AuthResponse
import retrofit2.Response

class UserRepository(
    private val api : MyApi,
    private val db:AppDatabase
) : SafeApiRequest(){
    suspend fun userLogin(email: String,password: String):AuthResponse{
        return apiRequest { api.userLogin(email, password) }
    }
    suspend fun saveUser(user: User) = db.getUserDao().upsert(user)

    fun getUser() = db.getUserDao().getuser()

}