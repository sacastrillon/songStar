package com.example.woo.songstar.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "SearchUser")
data class SearchUser (
    val search: String,
    @ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onDelete = ForeignKey.NO_ACTION
    )
    val userId: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)