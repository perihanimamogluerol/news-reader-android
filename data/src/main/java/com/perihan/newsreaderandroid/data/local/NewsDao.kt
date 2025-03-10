package com.perihan.newsreaderandroid.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.perihan.newsreaderandroid.data.local.entity.ArticleEntity

@Dao
interface NewsDao {
    @Query("SELECT * FROM news_articles")
    suspend fun fetchFavoriteArticles(): List<ArticleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: ArticleEntity)

    @Query("DELETE FROM news_articles WHERE title = :articleTitle")
    suspend fun deleteArticle(articleTitle: String)
}
