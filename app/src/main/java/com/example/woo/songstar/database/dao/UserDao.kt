package com.example.woo.songstar.database.dao

import androidx.room.*
import com.example.woo.songstar.models.User

@Dao
interface UserDao {
    @Query("SELECT * FROM User")
    fun getAll(): List<User>

    @Query("SELECT * FROM User WHERE username = :username and password = :password")
    fun getUser(username: String, password: String): List<User>

    @Query("SELECT * FROM User WHERE username = :username")
    fun getByUserName(username: String): List<User>

    @Insert
    fun insert(vararg user: User)

    @Update
    fun update(user: User)

    @Query("UPDATE User SET name = :name WHERE username = :username")
    fun updateName(name: String, username: String)

    @Query("UPDATE User SET language = :language WHERE username = :username")
    fun updateLanguage(language: String, username: String)

    @Query("UPDATE User SET password = :password WHERE username = :username")
    fun updatePassword(password: String, username: String)

    @Query("UPDATE User SET photo = :photo WHERE username = :username")
    fun updatePhoto(photo: String, username: String)

    @Delete
    fun delete(user: User)
}