package com.perihan.newsreaderandroid.data.remote.repository

import androidx.paging.PagingData
import com.perihan.newsreaderandroid.data.remote.response.ArticleResponse
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun fetchTopHeadlines(): Flow<PagingData<ArticleResponse>>
}