package com.example.woo.songstar.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "DetailArtistSong")
data class DetailArtistSong (
    @ForeignKey(
        entity = Artist::class,
        parentColumns = ["id"],
        childColumns = ["artistId"],
        onDelete = ForeignKey.NO_ACTION
    )
    val artistId: Int,
    @ForeignKey(
        entity = Song::class,
        parentColumns = ["id"],
        childColumns = ["songId"],
        onDelete = ForeignKey.NO_ACTION
    )
    val songId: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
): Serializable