package com.example.anivault.data.db.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

const val CURRENT_USER_ID = 0

@Entity
data class User(
    var id: Int? = null,
    var name: String? = null,
    var email: String? = null,
    var password: String? = null,
    var avatar: String? = null,
    var role_id: String? = null,
    var token:String? = null,
){
    @PrimaryKey(autoGenerate = false)
    var uid: Int = CURRENT_USER_ID
}