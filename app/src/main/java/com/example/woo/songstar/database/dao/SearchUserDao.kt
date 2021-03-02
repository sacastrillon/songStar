package com.example.woo.songstar.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.woo.songstar.models.SearchUser

@Dao
interface SearchUserDao {

    @Query("SELECT * FROM SearchUser WHERE userId = :userId LIMIT 10")
    fun getByUserId(userId: Int): List<SearchUser>

    @Insert
    fun insert(vararg searchUser: SearchUser)
}