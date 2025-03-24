package com.perihan.newsreaderandroid.data.remote.pagingsource

import androidx.paging.PagingSource
import com.perihan.newsreaderandroid.data.remote.NewsRemoteDataSource
import com.perihan.newsreaderandroid.data.remote.response.topheadline.ArticleResponse
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class SearchNewsPagingSourceFactory @AssistedInject constructor(
    private val remoteDataSource: NewsRemoteDataSource,
    @Assisted("sources") private val sources: String?,
    @Assisted("query") private val query: String
) : PagingSourceFactory<ArticleResponse> {
    override fun create(): PagingSource<Int, ArticleResponse> =
        SearchNewsPagingSource(remoteDataSource, sources, query)

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("sources") sources: String?, @Assisted("query") query: String
        ): SearchNewsPagingSourceFactory
    }
}