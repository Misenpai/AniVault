import com.example.anivault.data.db.Entities.User

data class AuthResponse(
    val result: AuthResult?,
    val token: String?
)

data class AuthResult(
    val issued_at: Double?,
    val payload: UserPayload?
)

data class UserPayload(
    val id: Int?,
    val name: String?,
    val email: String?,
    val avatar: String?,
    val phone: String?,
    val role_id: Int?
)

fun UserPayload.toUser(token: String?): User {
    return User(
        id = this.id,
        name = this.name,
        email = this.email,
        avatar = this.avatar,
        role_id = this.role_id,
        phone = this.phone,
        token = token
    )
}