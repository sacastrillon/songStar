package com.example.woo.songstar.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "User")
data class User (
    val name: String,
    val username: String,
    val password: String,
    var language: String,
    val photo: String,
    val isAdmin: Boolean,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
) : Serializable