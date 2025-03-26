package com.perihan.newsreaderandroid.domain

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import com.perihan.newsreaderandroid.data.repository.NewsRepository
import com.perihan.newsreaderandroid.domain.mapper.ArticleDomainMapper
import com.perihan.newsreaderandroid.domain.usecase.SearchNewsArticleUseCase
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

@OptIn(ExperimentalCoroutinesApi::class)
class SearchNewsArticleUseCaseTest {

    private val newsRepository: NewsRepository = mockk()
    private val articleDomainMapper: ArticleDomainMapper = mockk()
    private lateinit var useCase: SearchNewsArticleUseCase

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        useCase = SearchNewsArticleUseCase(newsRepository, articleDomainMapper)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN valid query and source WHEN searching THEN returns mapped ArticleDomainModel list`() =
        runTest {
            val articleResponse = articleResponse("News A")
            val pagingData = PagingData.from(listOf(articleResponse))

            coEvery { newsRepository.searchNewsArticle(null, "android") } returns flowOf(pagingData)

            val articleDomain = articleDomainModel("News A")
            every { articleDomainMapper.toDomain(articleResponse) } returns articleDomain

            val resultSnapshot = useCase(sources = null, query = "android").asSnapshot()

            assertEquals(1, resultSnapshot.size)
            assertEquals(articleDomain, resultSnapshot[0])

            coVerify(exactly = 1) { newsRepository.searchNewsArticle(null, "android") }
        }

    @Test
    fun `GIVEN empty query WHEN searching THEN returns empty result`() = runTest {
        coEvery { newsRepository.searchNewsArticle(null, "") } returns flowOf(PagingData.empty())

        val resultSnapshot = useCase(sources = null, query = "").asSnapshot()

        assertTrue(resultSnapshot.isEmpty())
        coVerify(exactly = 1) { newsRepository.searchNewsArticle(null, "") }
    }

    @Test
    fun `GIVEN query WHEN repository throws THEN propagates exception`() = runTest {
        coEvery {
            newsRepository.searchNewsArticle(
                null, "android"
            )
        } throws RuntimeException("Network error")

        try {
            useCase(sources = null, query = "android").asSnapshot()
            fail("Exception was expected but not thrown")
        } catch (e: RuntimeException) {
            assertEquals("Network error", e.message)
        }

        coVerify(exactly = 1) { newsRepository.searchNewsArticle(null, "android") }
    }

    @Test
    fun `GIVEN query WHEN mapping fails THEN propagates exception`() = runTest {
        val articleResponse = articleResponse("News A")
        val pagingData = PagingData.from(listOf(articleResponse))

        coEvery { newsRepository.searchNewsArticle(null, "android") } returns flowOf(pagingData)

        every { articleDomainMapper.toDomain(articleResponse) } throws RuntimeException("Mapping failed")

        try {
            useCase(sources = null, query = "android").asSnapshot()
            fail("Exception was expected but not thrown")
        } catch (e: RuntimeException) {
            assertEquals("Mapping failed", e.message)
        }

        coVerify(exactly = 1) { newsRepository.searchNewsArticle(null, "android") }
    }
}