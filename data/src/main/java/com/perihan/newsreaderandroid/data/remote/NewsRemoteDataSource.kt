package com.perihan.newsreaderandroid.data.remote

import com.perihan.newsreaderandroid.data.remote.response.NewsTopHeadlinesResponse

interface NewsRemoteDataSource {
    suspend fun fetchTopHeadlines(page: Int): NewsTopHeadlinesResponse
}