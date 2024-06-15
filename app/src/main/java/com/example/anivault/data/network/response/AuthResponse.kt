package com.example.anivault.data.network.response

data class AuthResponse(
    val result: AuthResult?,
    val token: String?
)

data class AuthResult(
    val issued_at: Double?,
    val payload: UserPayload?
)

data class UserPayload(
    val avatar: String?,
    val email: String?,
    val id: Int?,
    val name: String?,
    val phone: String?,
    val role_id: Int?
)
