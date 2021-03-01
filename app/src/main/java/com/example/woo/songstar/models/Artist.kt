package com.example.woo.songstar.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Artist")
data class Artist (
    val name: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
): Serializable