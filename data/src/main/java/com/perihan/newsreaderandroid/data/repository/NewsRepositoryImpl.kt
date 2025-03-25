package com.perihan.newsreaderandroid.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.perihan.newsreaderandroid.data.local.NewsLocalDataSource
import com.perihan.newsreaderandroid.data.local.entity.ArticleEntity
import com.perihan.newsreaderandroid.data.remote.NewsRemoteDataSource
import com.perihan.newsreaderandroid.data.remote.pagingsource.SearchNewsPagingSourceFactory
import com.perihan.newsreaderandroid.data.remote.pagingsource.TopHeadlinesPagingSourceFactory
import com.perihan.newsreaderandroid.data.remote.response.source.NewsSourcesResponse
import com.perihan.newsreaderandroid.data.remote.response.topheadline.ArticleResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class NewsRepositoryImpl @Inject constructor(
    private val newsRemoteDataSource: NewsRemoteDataSource,
    private val newsLocalDataSource: NewsLocalDataSource,
    private val topHeadlinesPagingSourceFactory: TopHeadlinesPagingSourceFactory,
    private val searchNewsPagingSourceFactory: SearchNewsPagingSourceFactory.Factory
) : NewsRepository {

    override fun fetchTopHeadlines(): Flow<PagingData<ArticleResponse>> {
        return flow {
            val favoriteArticleTitles = try {
                newsLocalDataSource.fetchFavoriteArticles().map { it.title }
            } catch (e: Exception) {
                emptyList()
            }
            emit(favoriteArticleTitles)
        }.flatMapLatest { favoriteArticleTitles ->
            Pager(config = PagingConfig(pageSize = 20, enablePlaceholders = false),
                pagingSourceFactory = { topHeadlinesPagingSourceFactory.create() }).flow.map { pagingData ->
                pagingData.map { article ->
                    article.copy(isFavorite = favoriteArticleTitles.contains(article.title))
                }
            }
        }
    }

    override fun searchNewsArticle(
        sources: String?, query: String
    ): Flow<PagingData<ArticleResponse>> {
        return flow {
            val favoriteArticleTitles = try {
                newsLocalDataSource.fetchFavoriteArticles().map { it.title }
            } catch (e: Exception) {
                emptyList()
            }
            emit(favoriteArticleTitles)
        }.flatMapLatest { favoriteArticleTitles ->
            Pager(config = PagingConfig(pageSize = 20, enablePlaceholders = false),
                pagingSourceFactory = {
                    searchNewsPagingSourceFactory.create(sources, query).create()
                }).flow.map { pagingData ->
                pagingData.map { article ->
                    article.copy(isFavorite = favoriteArticleTitles.contains(article.title))
                }
            }
        }
    }

    override fun insertArticle(article: ArticleEntity): Flow<Unit> = flow {
        newsLocalDataSource.insertArticle(article)
        emit(Unit)
    }

    override fun deleteArticle(articleTitle: String): Flow<Unit> = flow {
        newsLocalDataSource.deleteArticle(articleTitle = articleTitle)
        emit(Unit)
    }

    override fun fetchFavoriteArticles(): Flow<List<ArticleEntity>> = flow {
        emit(newsLocalDataSource.fetchFavoriteArticles())
    }

    override fun fetchNewsSources(): Flow<NewsSourcesResponse> = flow {
        emit(newsRemoteDataSource.fetchNewsSources())
    }
}