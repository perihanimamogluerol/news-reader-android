package com.perihan.newsreaderandroid.data.remote

import com.perihan.newsreaderandroid.data.remote.response.source.NewsSourcesResponse
import com.perihan.newsreaderandroid.data.remote.response.topheadline.NewsTopHeadlinesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("top-headlines")
    suspend fun fetchTopHeadlines(
        @Query("country") country: String = "us", @Query("page") page: Int
    ): NewsTopHeadlinesResponse

    @GET("everything")
    suspend fun searchNewsArticle(
        @Query("sources") sources: String? = null,
        @Query("q") query: String,
        @Query("page") page: Int
    ): NewsTopHeadlinesResponse

    @GET("top-headlines/sources")
    suspend fun fetchNewsSources(
        @Query("country") country: String = "us"
    ): NewsSourcesResponse
}