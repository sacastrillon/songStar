package com.example.woo.songstar.components

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.woo.songstar.models.Song

@Dao
interface SongDao {

    @Query("SELECT * FROM Song")
    fun getAll(): List<Song>

    @Query("SELECT * FROM Song WHERE name = :name")
    fun getBySongName(name: String): List<Song>

    @Query("SELECT * FROM Song WHERE artistId = :artistId")
    fun getBySongsByArtist(artistId: Int): List<Song>

    @Insert
    fun insert(vararg song: Song)
}