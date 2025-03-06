package com.perihan.newsreaderandroid.data.remote.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.perihan.newsreaderandroid.data.remote.NewsRemoteDataSource
import com.perihan.newsreaderandroid.data.remote.response.ArticleResponse
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class NewsRepositoryImpl @Inject constructor(private val newsRemoteDataSource: NewsRemoteDataSource) :
    NewsRepository {
    override fun fetchTopHeadlines(): Flow<PagingData<ArticleResponse>> {
        return Pager(config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { TopHeadlinesPagingSource(newsRemoteDataSource) }).flow
    }

    override fun searchNews(query: String): Flow<PagingData<ArticleResponse>> {
        return Pager(config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { SearchNewsPagingSource(newsRemoteDataSource, query) }).flow
    }
}