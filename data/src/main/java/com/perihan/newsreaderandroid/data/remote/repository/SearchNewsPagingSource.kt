package com.perihan.newsreaderandroid.data.remote.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.perihan.newsreaderandroid.data.remote.NewsRemoteDataSource
import com.perihan.newsreaderandroid.data.remote.response.ArticleResponse

class SearchNewsPagingSource(
    private val newsRemoteDataSource: NewsRemoteDataSource,
    private val query: String
) : PagingSource<Int, ArticleResponse>() {
    override fun getRefreshKey(state: PagingState<Int, ArticleResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val closestPage = state.closestPageToPosition(anchorPosition)
            closestPage?.prevKey?.plus(1) ?: closestPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleResponse> {
        val page = params.key ?: 1
        return try {
            val response = newsRemoteDataSource.searchNews(
                query = query, page = page
            )
            val articles = response.articles ?: emptyList()

            LoadResult.Page(
                data = articles,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (articles.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}