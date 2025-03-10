package com.perihan.newsreaderandroid.data.remote

import com.perihan.newsreaderandroid.data.remote.response.source.NewsSourcesResponse
import com.perihan.newsreaderandroid.data.remote.response.topheadline.NewsTopHeadlinesResponse
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class NewsRemoteDataSourceImpl @Inject constructor(private val newsApi: NewsApi) :
    NewsRemoteDataSource {
    override suspend fun fetchTopHeadlines(page: Int): NewsTopHeadlinesResponse =
        newsApi.fetchTopHeadlines(page = page)

    override suspend fun searchNewsArticle(
        sources: String?, query: String, page: Int
    ): NewsTopHeadlinesResponse = newsApi.searchNewsArticle(sources = sources, query = query, page = page)

    override suspend fun fetchNewsSources(): NewsSourcesResponse = newsApi.fetchNewsSources()
}