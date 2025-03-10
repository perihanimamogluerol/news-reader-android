package com.perihan.newsreaderandroid.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.perihan.newsreaderandroid.data.local.entity.ArticleEntity

@Database(entities = [ArticleEntity::class], version = 1, exportSchema = false)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao

    companion object {
        const val DATABASE_NAME = "news_database"
    }
}