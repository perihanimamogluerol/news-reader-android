package com.perihan.newsreaderandroid.data.remote

import com.perihan.newsreaderandroid.data.remote.response.source.NewsSourcesResponse
import com.perihan.newsreaderandroid.data.remote.response.topheadline.NewsTopHeadlinesResponse

interface NewsRemoteDataSource {
    suspend fun fetchTopHeadlines(page: Int): NewsTopHeadlinesResponse
    suspend fun searchNewsArticle(sources: String?, query: String, page: Int): NewsTopHeadlinesResponse
    suspend fun fetchNewsSources(): NewsSourcesResponse
}