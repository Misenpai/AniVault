package com.example.anivault.data.repository


import AuthResponse
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.anivault.data.db.AppDatabase
import com.example.anivault.data.db.Entities.User
import com.example.anivault.data.db.UserDao
import com.example.anivault.data.network.MyApi
import com.example.anivault.data.network.SafeApiRequest
import com.example.anivault.data.network.response.AnimeStatusData
import com.example.anivault.data.network.response.AnimeStatusListResponse
import com.example.anivault.data.network.response.MessageResponse
import toUser

class UserRepository(
    private val api: MyApi,
    private val db: AppDatabase,
    private val userDao: UserDao

) : SafeApiRequest() {
    suspend fun userLogin(email: String, password: String): AuthResponse {
        val response = apiRequest { api.userLogin(email, password) }
        Log.d("UserRepository", "Login response: $response")
        saveUserFromResponse(response)
        return response
    }

    fun getCurrentUser(): LiveData<User> {
        return userDao.getAnyUser()
    }


    suspend fun userSignup(name: String, email: String, password: String): AuthResponse {
        val response = apiRequest { api.usersignup(name, email, password) }
        Log.d("UserRepository", "Signup response: $response")
        saveUserFromResponse(response)
        return response
    }

    private suspend fun saveUserFromResponse(response: AuthResponse) {
        response.result?.payload?.let { userPayload ->
            val user = userPayload.toUser(response.token)
            saveUser(user)
        }
    }

    suspend fun saveUser(user: User) {
        val result = db.getUserDao().upsert(user)
        Log.d("UserRepository", "User save result: $result")
    }

    fun getUser(userId: Int) = db.getUserDao().getUser(userId)

    fun getAnyUser() = db.getUserDao().getAnyUser()

    suspend fun getAllUsers(): List<User> {
        return db.getUserDao().getAllUsers()
    }

    suspend fun insertAnimeStatus(data: AnimeStatusData): MessageResponse {
        return apiRequest { api.insertAnimeStatus(data) }
    }

    suspend fun updateAnimeStatus(data: AnimeStatusData): MessageResponse {
        return apiRequest { api.updateAnimeStatus(data) }
    }

    suspend fun removeAnimeStatus(userId: Int, malId: Int): MessageResponse {
        return apiRequest { api.removeAnimeStatus(userId, malId) }
    }

    suspend fun readAnimeStatus(userId: Int, status: String): AnimeStatusListResponse {
        return apiRequest { api.readAnimeStatus(userId, status) }
    }
}
