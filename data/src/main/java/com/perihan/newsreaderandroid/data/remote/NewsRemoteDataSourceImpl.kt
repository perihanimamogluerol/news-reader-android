package com.perihan.newsreaderandroid.data.remote

import com.perihan.newsreaderandroid.data.remote.response.NewsTopHeadlinesResponse
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class NewsRemoteDataSourceImpl @Inject constructor(private val newsApi: NewsApi) :
    NewsRemoteDataSource {
    override suspend fun fetchTopHeadlines(page: Int): NewsTopHeadlinesResponse =
        newsApi.fetchTopHeadlines(page = page)

    override suspend fun searchNews(query: String, page: Int): NewsTopHeadlinesResponse =
        newsApi.searchNews(query = query, page = page)
}