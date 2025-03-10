package com.perihan.newsreaderandroid.data.local

import com.perihan.newsreaderandroid.data.local.entity.ArticleEntity

interface NewsLocalDataSource {
    suspend fun fetchFavoriteArticles(): List<ArticleEntity>
    suspend fun insertArticle(article: ArticleEntity)
    suspend fun deleteArticle(articleTitle: String)
}