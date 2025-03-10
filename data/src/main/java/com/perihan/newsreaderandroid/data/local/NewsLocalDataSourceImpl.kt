package com.perihan.newsreaderandroid.data.local

import com.perihan.newsreaderandroid.data.local.entity.ArticleEntity
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class NewsLocalDataSourceImpl @Inject constructor(private val newsDao: NewsDao) :
    NewsLocalDataSource {
    override suspend fun fetchFavoriteArticles(): List<ArticleEntity> =
        newsDao.fetchFavoriteArticles()

    override suspend fun insertArticle(article: ArticleEntity) =
        newsDao.insertArticle(article = article)

    override suspend fun deleteArticle(articleTitle: String) =
        newsDao.deleteArticle(articleTitle = articleTitle)
}