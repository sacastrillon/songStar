package com.example.woo.songstar.components

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.woo.songstar.models.MostListenedSong
import com.example.woo.songstar.models.Song

@Dao
interface MostListenedSongDao {

    @Query("SELECT * FROM Song WHERE id IN (SELECT songId FROM MostListenedSong WHERE userId = :userId ORDER BY times LIMIT 5)")
    fun getMostListenedSongsByUser(userId: Int): List<Song>

    @Insert
    fun insert(vararg mostListenedSong: MostListenedSong)
}