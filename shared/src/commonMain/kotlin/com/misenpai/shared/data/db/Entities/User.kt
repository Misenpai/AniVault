package com.misenpai.shared.data.db.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

const val CURRENT_USER_ID = 0

@Entity
data class User(
    @PrimaryKey var id: Int? = null,
    var name: String? = null,
    var email: String? = null,
    var password: String? = null,
    var avatar: String? = null,
    var role_id: Int? = null,
    var phone: String? = null,
    var token: String? = null
)