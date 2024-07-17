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
        saveUserFromResponse(response)
        return response
    }

    suspend fun userSignup(name: String, email: String, password: String): AuthResponse {
        val response = apiRequest { api.usersignup(name, email, password) }
        saveUserFromResponse(response)
        return response
    }

    private suspend fun saveUserFromResponse(response: AuthResponse) {
        response.result?.payload?.let { userPayload ->
            val user = userPayload.toUser(response.token)
            saveUser(user)
        }
    }

    suspend fun saveUser(user: User) = db.getUserDao().upsert(user)

    fun getUser(userId: Int) = db.getUserDao().getUser(userId)

    fun getAnyUser() = db.getUserDao().getAnyUser()

}