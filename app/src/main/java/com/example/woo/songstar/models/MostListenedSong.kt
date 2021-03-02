package com.example.woo.songstar.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "MostListenedSong")
data class MostListenedSong (
    @ForeignKey(
        entity = Song::class,
        parentColumns = ["id"],
        childColumns = ["songId"],
        onDelete = ForeignKey.NO_ACTION
    )
    val songId: Int,
    @ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onDelete = ForeignKey.NO_ACTION
    )
    val userId: Int,
    val times: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0

)