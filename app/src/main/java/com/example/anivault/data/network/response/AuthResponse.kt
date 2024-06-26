package com.example.anivault.data.network.response

import com.example.anivault.data.db.Entities.User

data class AuthResponse(
    val result: AuthResult?,
    val token: String?,
    val message: String?
)

data class AuthResult(
    val issued_at: Double?,
    val payload: User?
)
