package com.misenpai.shared.data.repository


import AuthResponse
import android.util.Log
import androidx.lifecycle.LiveData
import com.misenpai.anivault.data.db.AppDatabase
import com.misenpai.anivault.data.db.Entities.User
import com.misenpai.anivault.data.db.UserDao
import com.misenpai.anivault.data.network.MyApi
import com.misenpai.anivault.data.network.SafeApiRequest
import com.misenpai.anivault.data.network.response.AnimeStatusData
import com.misenpai.anivault.data.network.response.AnimeStatusListResponse
import com.misenpai.anivault.data.network.response.AnimeStatusUpdateData
import com.misenpai.anivault.data.network.response.MessageResponse
import com.misenpai.anivault.data.network.response.MessageResponseUpdate
import retrofit2.Response
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


    suspend fun userSignup(name: String, email: String, password: String): Result<AuthResponse> {
        return try {
            val response = api.usersignup(name, email, password)
            Log.d("UserRepository", "Signup response: ${response.code()} - ${response.message()}")

            when {
                response.isSuccessful -> {
                    response.body()?.let { authResponse ->
                        saveUserFromResponse(authResponse)
                        Result.success(authResponse)
                    } ?: Result.failure(Exception("Empty response body"))
                }
                response.code() == 409 -> {
                    Result.failure(Exception("User with this email or name already exists"))
                }
                else -> {
                    Result.failure(Exception("Signup failed: ${response.message()}"))
                }
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Signup error", e)
            Result.failure(e)
        }
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

    suspend fun insertAnimeStatus(data: AnimeStatusData): Response<MessageResponse> {
        return api.insertAnimeStatus(data)
    }

    suspend fun updateAnimeStatus(data: AnimeStatusUpdateData): Response<MessageResponseUpdate> {
        val response = api.updateAnimeStatus(data)
        Log.e("UpdateResponse", "Update response: ${response.code()} - ${response.message()}")
        return response
    }

    suspend fun removeAnimeStatus(userId: Int, malId: Int): Response<MessageResponse> {
        return api.removeAnimeStatus(userId, malId)
    }

    suspend fun readAnimeStatus(userId: Int, status: String): AnimeStatusListResponse {
        return apiRequest { api.readAnimeStatus(userId, status) }
    }

    suspend fun clearAllUserData() {
        userDao.deleteAllUsers()
        // Clear any other related data if necessary
    }


}
