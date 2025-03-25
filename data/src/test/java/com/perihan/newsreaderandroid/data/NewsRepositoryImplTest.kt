package com.perihan.newsreaderandroid.data

import android.util.Log
import androidx.paging.testing.asSnapshot
import com.perihan.newsreaderandroid.data.local.NewsLocalDataSource
import com.perihan.newsreaderandroid.data.remote.NewsRemoteDataSource
import com.perihan.newsreaderandroid.data.remote.pagingsource.SearchNewsPagingSourceFactory
import com.perihan.newsreaderandroid.data.remote.pagingsource.TopHeadlinesPagingSourceFactory
import com.perihan.newsreaderandroid.data.remote.response.topheadline.ArticleResponse
import com.perihan.newsreaderandroid.data.repository.NewsRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class NewsRepositoryImplTest {

    private val newsLocalDataSource: NewsLocalDataSource = mockk()
    private val newsRemoteDataSource: NewsRemoteDataSource = mockk()
    private val topHeadlinesPagingSourceFactory: TopHeadlinesPagingSourceFactory = mockk()
    private val searchNewsPagingSourceFactory: SearchNewsPagingSourceFactory.Factory = mockk()
    private lateinit var repository: NewsRepositoryImpl

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockkStatic(Log::class)
        every { Log.isLoggable(any(), any()) } returns false
        repository = NewsRepositoryImpl(
            newsRemoteDataSource,
            newsLocalDataSource,
            topHeadlinesPagingSourceFactory,
            searchNewsPagingSourceFactory
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN remote articles and local favorites WHEN fetching top headlines THEN maps isFavorite correctly`() =
        runTest {
            val favoriteArticles = favoriteArticles("News A", "News B")
            coEvery { newsLocalDataSource.fetchFavoriteArticles() } returns favoriteArticles

            val remoteArticles = remoteArticles("News A", "News C")
            every { topHeadlinesPagingSourceFactory.create() } returns FakePagingSource(
                remoteArticles
            )

            val snapshot = repository.fetchTopHeadlines().asSnapshot()

            assertEquals(2, snapshot.size)
            assertTrue(snapshot[0].isFavorite)
            assertFalse(snapshot[1].isFavorite)

            coVerify(exactly = 1) { newsLocalDataSource.fetchFavoriteArticles() }
        }

    @Test
    fun `GIVEN remote articles and NO local favorites WHEN fetching top headlines THEN all isFavorite are false`() =
        runTest {
            coEvery { newsLocalDataSource.fetchFavoriteArticles() } returns emptyList()

            val remoteArticles = remoteArticles("News A", "News C")
            every { topHeadlinesPagingSourceFactory.create() } returns FakePagingSource(
                remoteArticles
            )

            val snapshot = repository.fetchTopHeadlines().asSnapshot()

            assertFalse(snapshot[0].isFavorite)
            assertFalse(snapshot[1].isFavorite)
        }

    @Test
    fun `GIVEN empty remote articles WHEN fetching top headlines THEN returns empty snapshot`() =
        runTest {
            coEvery { newsLocalDataSource.fetchFavoriteArticles() } returns emptyList()

            val remoteArticles = emptyList<ArticleResponse>()
            every { topHeadlinesPagingSourceFactory.create() } returns FakePagingSource(
                remoteArticles
            )

            val snapshot = repository.fetchTopHeadlines().asSnapshot()

            assertTrue(snapshot.isEmpty())
        }

    @Test
    fun `GIVEN local favorites throws exception WHEN fetching THEN handles gracefully`() = runTest {
        coEvery { newsLocalDataSource.fetchFavoriteArticles() } throws RuntimeException("DB Error")

        val remoteArticles = remoteArticles("News A")
        every { topHeadlinesPagingSourceFactory.create() } returns FakePagingSource(remoteArticles)

        val snapshot = repository.fetchTopHeadlines().asSnapshot()

        assertEquals(1, snapshot.size)
        assertFalse(snapshot[0].isFavorite)
    }
}