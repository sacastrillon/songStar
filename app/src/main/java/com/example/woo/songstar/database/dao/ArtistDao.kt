package com.example.woo.songstar.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.woo.songstar.models.Artist

@Dao
interface ArtistDao {

    @Query("SELECT * FROM Artist")
    fun getAll(): List<Artist>

    @Query("SELECT * FROM Artist WHERE name = :name")
    fun getByArtistName(name: String): List<Artist>

    @Query("SELECT * FROM Artist WHERE id = :id")
    fun getArtistById(id: Int): List<Artist>

    @Insert
    fun insert(vararg artist: Artist)
}