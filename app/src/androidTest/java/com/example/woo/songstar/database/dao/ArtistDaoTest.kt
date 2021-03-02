package com.example.woo.songstar.database.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.woo.songstar.database.AppDatabase
import com.example.woo.songstar.models.Artist
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class ArtistDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var artistDao: ArtistDao

    @Before
    fun setup() {
        this.db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        this.artistDao = this.db.artistDao()
    }

    @After
    fun closeDB() {
        this.db.close()
    }

    @Test
    fun insertArtist() {
        val artist = Artist("artist1", id = 1)
        db.artistDao().insert(artist)
        val artists = db.artistDao().getAll()
        assertThat(artists.contains(artist))
    }
}