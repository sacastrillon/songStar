package com.example.woo.songstar.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Song")
data class Song (
    val name: String,
    val album: String,
    @ForeignKey(
        entity = Artist::class,
        parentColumns = ["id"],
        childColumns = ["artistId"],
        onDelete = ForeignKey.NO_ACTION
    )
    val artistId: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
): Serializable