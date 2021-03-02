package com.example.woo.songstar.database.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.woo.songstar.database.AppDatabase
import com.example.woo.songstar.models.Artist
import com.example.woo.songstar.models.FavouriteSong
import com.example.woo.songstar.models.Song
import com.example.woo.songstar.models.User
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class FavouriteSongDaoTest {

    private lateinit var db: AppDatabase

    @Before
    fun setup() {
        this.db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun closeDB() {
        this.db.close()
    }

    @Test
    fun insertFavouriteSong() {
        val user = User("Admin", "admin_user", "qwery123$", "English", "", true, id = 1)
        db.userDao().insert(user)
        val artist = Artist("artist1", id = 1)
        db.artistDao().insert(artist)
        val song = Song("song1", "album1", 1, id = 1)
        db.songDao().insert(song)

        val favouriteSong = FavouriteSong(1, 1)
        db.favouriteSongDao().insert(favouriteSong)
        val favouriteSongs = db.favouriteSongDao().getFavouriteSongsByUserId(1)
        assertThat(favouriteSongs.contains(song))
    }

    @Test
    fun deleteFavouriteSong() {
        val user = User("Admin", "admin_user", "qwery123$", "English", "", true, id = 1)
        db.userDao().insert(user)
        val artist = Artist("artist1", id = 1)
        db.artistDao().insert(artist)
        val song = Song("song1", "album1", 1, id = 1)
        db.songDao().insert(song)

        val favouriteSong = FavouriteSong(1, 1, id = 1)
        db.favouriteSongDao().insert(favouriteSong)
        db.favouriteSongDao().delete(favouriteSong)
        val favouriteSongs = db.favouriteSongDao().getFavouriteSongsByUserId(1)
        assertThat(!favouriteSongs.contains(song))
    }

}