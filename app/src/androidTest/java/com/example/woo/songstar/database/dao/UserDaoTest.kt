package com.example.woo.songstar.database.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.woo.songstar.database.AppDatabase
import com.example.woo.songstar.models.User
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class UserDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var userDao: UserDao

    @Before
    fun setup() {
        this.db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        this.userDao = this.db.userDao()
    }

    @After
    fun closeDB() {
        this.db.close()
    }

    @Test
    fun insertUser() {
        val user = User("Admin", "admin_user", "qwery123$", "English", "", true)
        db.userDao().insert(user)
        val users = db.userDao().getAll()
        assertThat(users.contains(user))
    }

    @Test
    fun deleteUser() {
        val user = User("Admin", "admin_user", "qwery123$", "English", "", true)
        db.userDao().insert(user)
        db.userDao().delete(user)
        val users = db.userDao().getAll()
        assertThat(!users.contains(user))
    }

    @Test
    fun updateNameUser() {
        val user = User("Admin", "admin_user", "qwery123$", "English", "", true)
        db.userDao().insert(user)
        db.userDao().updateName("admin2", "admin_user")
        val users = db.userDao().getAll()
        assertThat(users.first().name).isEqualTo("admin2")

    }

    @Test
    fun updateLanguageUser() {
        val user = User("Admin", "admin_user", "qwery123$", "English", "", true)
        db.userDao().insert(user)
        db.userDao().updateLanguage("Spanish", "admin_user")
        val users = db.userDao().getAll()
        assertThat(users.first().language).isEqualTo("Spanish")
    }
}