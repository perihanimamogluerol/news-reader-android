package com.perihan.newsreaderandroid.data.remote.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.perihan.newsreaderandroid.data.remote.NewsRemoteDataSource
import com.perihan.newsreaderandroid.data.remote.response.topheadline.ArticleResponse

class TopHeadlinesPagingSource(
    private val newsRemoteDataSource: NewsRemoteDataSource
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
            val response = newsRemoteDataSource.fetchTopHeadlines(
                page = page
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