package com.perihan.newsreaderandroid.domain

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import com.perihan.newsreaderandroid.data.repository.NewsRepository
import com.perihan.newsreaderandroid.domain.mapper.ArticleDomainMapper
import com.perihan.newsreaderandroid.domain.usecase.FetchTopHeadlinesUseCase
import com.perihan.newsreaderandroid.domain.utils.articleDomainModel
import com.perihan.newsreaderandroid.domain.utils.articleResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class FetchTopHeadlinesUseCaseTest {

    private val newsRepository: NewsRepository = mockk()
    private val articleDomainMapper: ArticleDomainMapper = mockk()
    private lateinit var useCase: FetchTopHeadlinesUseCase

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        useCase = FetchTopHeadlinesUseCase(newsRepository, articleDomainMapper)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN remote articles WHEN fetching top headlines THEN returns mapped ArticleDomainModel list`() =
        runTest {
            val articleResponse = articleResponse("News A")
            val pagingData = PagingData.from(listOf(articleResponse))

            coEvery { newsRepository.fetchTopHeadlines() } returns flowOf(pagingData)

            val articleDomain = articleDomainModel("News A")
            every { articleDomainMapper.toDomain(articleResponse) } returns articleDomain

            val resultSnapshot = useCase().asSnapshot()

            assertEquals(1, resultSnapshot.size)
            assertEquals(articleDomain, resultSnapshot[0])

            coVerify(exactly = 1) { newsRepository.fetchTopHeadlines() }
        }

    @Test
    fun `GIVEN repository returns empty list WHEN fetching top headlines THEN returns empty PagingData`() =
        runTest {
            coEvery { newsRepository.fetchTopHeadlines() } returns flowOf(PagingData.empty())

            val resultSnapshot = useCase().asSnapshot()

            assertTrue(resultSnapshot.isEmpty())

            coVerify(exactly = 1) { newsRepository.fetchTopHeadlines() }
        }

    @Test
    fun `GIVEN repository throws exception WHEN fetching top headlines THEN propagates the exception`() =
        runTest {
            coEvery { newsRepository.fetchTopHeadlines() } throws RuntimeException("Repository error")

            try {
                useCase().asSnapshot()
                fail("Exception was expected but not thrown")
            } catch (e: RuntimeException) {
                assertEquals("Repository error", e.message)
            }

            coVerify(exactly = 1) { newsRepository.fetchTopHeadlines() }
        }

    @Test
    fun `GIVEN mapping throws exception WHEN fetching top headlines THEN propagates the exception`() =
        runTest {
            val articleResponse = articleResponse("News A")
            val pagingData = PagingData.from(listOf(articleResponse))

            coEvery { newsRepository.fetchTopHeadlines() } returns flowOf(pagingData)

            every { articleDomainMapper.toDomain(articleResponse) } throws RuntimeException("Mapping failed")

            try {
                useCase().asSnapshot()
                fail("Exception was expected but not thrown")
            } catch (e: RuntimeException) {
                assertEquals("Mapping failed", e.message)
            }

            coVerify(exactly = 1) { newsRepository.fetchTopHeadlines() }
        }

    @Test
    fun `GIVEN multiple remote articles WHEN fetching top headlines THEN returns mapped ArticleDomainModel list`() =
        runTest {
            val response1 = articleResponse("News A")
            val response2 = articleResponse("News B")
            val pagingData = PagingData.from(listOf(response1, response2))

            coEvery { newsRepository.fetchTopHeadlines() } returns flowOf(pagingData)

            val domain1 = articleDomainModel("News A")
            val domain2 = articleDomainModel("News B")

            every { articleDomainMapper.toDomain(response1) } returns domain1
            every { articleDomainMapper.toDomain(response2) } returns domain2

            val resultSnapshot = useCase().asSnapshot()

            assertEquals(2, resultSnapshot.size)
            assertEquals(listOf(domain1, domain2), resultSnapshot)
        }
}