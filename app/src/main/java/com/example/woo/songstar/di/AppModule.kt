package com.example.woo.songstar.di

import android.content.Context
import androidx.room.Room
import com.example.woo.songstar.database.AppDatabase
import com.example.woo.songstar.intefaces.ApiInterface
import com.example.woo.songstar.utils.AppUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRunningDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "database"
    ).build()

    @Singleton
    @Provides
    fun provideRetrofitInstance(): ApiInterface = AppUtils.getRetrofitInstance().create(ApiInterface::class.java)


    @Singleton
    @Provides
    fun provideSongDao(db: AppDatabase) = db.songDao()

    @Singleton
    @Provides
    fun provideUserDao(db: AppDatabase) = db.userDao()

    @Singleton
    @Provides
    fun provideArtistDao(db: AppDatabase) = db.artistDao()

    @Singleton
    @Provides
    fun provideSearchUserDao(db: AppDatabase) = db.searchUserDao()

    @Singleton
    @Provides
    fun provideFavouriteSongDao(db: AppDatabase) = db.favouriteSongDao()

    @Singleton
    @Provides
    fun provideMostListenedSongDao(db: AppDatabase) = db.mostListenedSongDao()

}