import android.util.Log
import com.example.anivault.data.db.AppDatabase
import com.example.anivault.data.db.Entities.User
import com.example.anivault.data.network.MyApi
import com.example.anivault.data.network.SafeApiRequest

class UserRepository(
    private val api: MyApi,
    private val db: AppDatabase
) : SafeApiRequest() {
    suspend fun userLogin(email: String, password: String): AuthResponse {
        val response = apiRequest { api.userLogin(email, password) }
        Log.d("UserRepository", "Login response: $response")
        saveUserFromResponse(response)
        return response
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
}
