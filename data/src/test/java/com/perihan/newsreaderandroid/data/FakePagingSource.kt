package com.perihan.newsreaderandroid.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.perihan.newsreaderandroid.data.remote.response.topheadline.ArticleResponse

class FakePagingSource(
    private val articles: List<ArticleResponse>
) : PagingSource<Int, ArticleResponse>() {

    override fun getRefreshKey(state: PagingState<Int, ArticleResponse>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleResponse> {
        return LoadResult.Page(
            data = articles, prevKey = null, nextKey = null
        )
    }
}