package com.perihan.newsreaderandroid.data.remote

import com.perihan.newsreaderandroid.data.remote.response.NewsTopHeadlinesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("top-headlines")
    suspend fun fetchTopHeadlines(
        @Query("country") country: String = "us",
        @Query("page") page: Int
    ): NewsTopHeadlinesResponse

    @GET("everything")
    suspend fun searchNews(
        @Query("q") query: String,
        @Query("page") page: Int
    ): NewsTopHeadlinesResponse
}