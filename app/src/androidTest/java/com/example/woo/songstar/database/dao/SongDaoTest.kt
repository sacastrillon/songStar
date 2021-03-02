package com.example.woo.songstar.database.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.woo.songstar.database.AppDatabase
import com.example.woo.songstar.models.Artist
import com.example.woo.songstar.models.Song
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class SongDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var songDao: SongDao

    @Before
    fun setup() {
        this.db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        this.songDao = this.db.songDao()
    }

    @After
    fun closeDB() {
        this.db.close()
    }

    @Test
    fun insertSong() {
        val artist = Artist("artist1")
        val song = Song("song1", "album1", 1)
        db.artistDao().insert(artist)
        db.songDao().insert(song)
        val songs = db.songDao().getAll()
        assertThat(songs.contains(song))
    }

}