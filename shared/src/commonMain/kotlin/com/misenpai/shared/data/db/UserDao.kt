package com.misenpai.shared.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.misenpai.anivault.data.db.Entities.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(user: User): Long

    @Query("SELECT * FROM user WHERE id = :userId")
    fun getUser(userId: Int): LiveData<User>

    @Query("SELECT * FROM user LIMIT 1")
    fun getAnyUser(): LiveData<User>

    @Query("SELECT * FROM User")
    fun getAllUsers(): List<User>

    @Query("DELETE FROM user")
    suspend fun deleteAllUsers()

}