package com.perihan.newsreaderandroid.data.remote.pagingsource

import androidx.paging.PagingSource
import com.perihan.newsreaderandroid.data.remote.NewsRemoteDataSource
import com.perihan.newsreaderandroid.data.remote.response.topheadline.ArticleResponse
import javax.inject.Inject

class TopHeadlinesPagingSourceFactory @Inject constructor(
    private val remoteDataSource: NewsRemoteDataSource
) : PagingSourceFactory<ArticleResponse> {
    override fun create(): PagingSource<Int, ArticleResponse> =
        TopHeadlinesPagingSource(remoteDataSource)
}