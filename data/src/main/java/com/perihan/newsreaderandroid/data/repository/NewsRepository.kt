package com.perihan.newsreaderandroid.data.repository

import androidx.paging.PagingData
import com.perihan.newsreaderandroid.data.local.entity.ArticleEntity
import com.perihan.newsreaderandroid.data.remote.response.source.NewsSourcesResponse
import com.perihan.newsreaderandroid.data.remote.response.topheadline.ArticleResponse
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun fetchTopHeadlines(): Flow<PagingData<ArticleResponse>>
    fun searchNewsArticle(sources: String?, query: String): Flow<PagingData<ArticleResponse>>
    fun insertArticle(article: ArticleEntity): Flow<Unit>
    fun deleteArticle(articleTitle: String): Flow<Unit>
    fun fetchFavoriteArticles(): Flow<List<ArticleEntity>>
    fun fetchNewsSources(): Flow<NewsSourcesResponse>
}