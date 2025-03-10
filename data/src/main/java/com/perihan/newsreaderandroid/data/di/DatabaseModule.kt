package com.perihan.newsreaderandroid.data.di

import android.content.Context
import androidx.room.Room
import com.perihan.newsreaderandroid.data.local.NewsDao
import com.perihan.newsreaderandroid.data.local.NewsDatabase
import com.perihan.newsreaderandroid.data.local.NewsDatabase.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): NewsDatabase {
        return Room.databaseBuilder(
            context.applicationContext, NewsDatabase::class.java, DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideNewsDao(database: NewsDatabase): NewsDao {
        return database.newsDao()
    }
}