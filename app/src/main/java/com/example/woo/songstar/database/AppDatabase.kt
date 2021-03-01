package com.example.woo.songstar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.woo.songstar.components.*
import com.example.woo.songstar.models.*

@Database(entities = [User::class, Artist::class, Song::class, DetailArtistSong::class, SearchUser::class, FavouriteSong::class],
    version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun artistDao(): ArtistDao
    abstract fun songDao(): SongDao
    abstract fun searchUserDao(): SearchUserDao
    abstract fun favouriteSongDao(): FavoriteSongDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "database").build()
                INSTANCE = instance
                return instance
            }
        }
    }
}