package com.example.woo.songstar.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.woo.songstar.models.FavouriteSong
import com.example.woo.songstar.models.Song

@Dao
interface FavoriteSongDao {

    @Query("SELECT * FROM FavouriteSong WHERE userId = :userId and songId = :songId")
    fun getFavoriteSong(userId: Int, songId: Int): List<FavouriteSong>

    @Query("SELECT * FROM Song WHERE id IN (SELECT songId FROM FavouriteSong WHERE userId = :userId)")
    fun getFavouriteSongsByUserId(userId: Int): List<Song>

    @Insert
    fun insert(vararg favouriteSong: FavouriteSong)

    @Delete
    fun delete(favouriteSong: FavouriteSong)
}