package com.perihan.newsreaderandroid.domain

import com.perihan.newsreaderandroid.data.repository.NewsRepository
import com.perihan.newsreaderandroid.domain.mapper.ArticleDomainMapper
import com.perihan.newsreaderandroid.domain.usecase.FetchFavoriteArticlesUseCase
import com.perihan.newsreaderandroid.domain.utils.articleDomainModel
import com.perihan.newsreaderandroid.domain.utils.articleEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
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
class FetchFavoriteArticlesUseCaseTest {

    private val newsRepository: NewsRepository = mockk()
    private val articleDomainMapper: ArticleDomainMapper = mockk()
    private lateinit var useCase: FetchFavoriteArticlesUseCase

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        useCase = FetchFavoriteArticlesUseCase(newsRepository, articleDomainMapper)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN favorite articles exist WHEN fetching THEN returns mapped list`() = runTest {
        val articleEntity = articleEntity("News A")
        coEvery { newsRepository.fetchFavoriteArticles() } returns flowOf(listOf(articleEntity))

        val articleDomainModel = articleDomainModel("News A")
        every { articleDomainMapper.toLocalDomain(articleEntity) } returns articleDomainModel

        val result = useCase().first()

        assertEquals(1, result.size)
        assertEquals(articleDomainModel, result[0])
        coVerify(exactly = 1) { newsRepository.fetchFavoriteArticles() }
    }

    @Test
    fun `GIVEN empty favorite list WHEN fetching THEN returns empty list`() = runTest {
        coEvery { newsRepository.fetchFavoriteArticles() } returns flowOf(emptyList())

        val result = useCase().first()

        assertTrue(result.isEmpty())
        coVerify(exactly = 1) { newsRepository.fetchFavoriteArticles() }
    }

    @Test
    fun `GIVEN repository throws exception WHEN fetching THEN propagates exception`() = runTest {
        coEvery { newsRepository.fetchFavoriteArticles() } throws RuntimeException("DB error")

        try {
            useCase().first()
            fail("Exception was expected but not thrown")
        } catch (e: RuntimeException) {
            assertEquals("DB error", e.message)
        }

        coVerify(exactly = 1) { newsRepository.fetchFavoriteArticles() }
    }

    @Test
    fun `GIVEN mapper throws exception WHEN mapping THEN propagates exception`() = runTest {
        val articleEntity = articleEntity("News A")
        coEvery { newsRepository.fetchFavoriteArticles() } returns flowOf(listOf(articleEntity))

        every { articleDomainMapper.toLocalDomain(articleEntity) } throws RuntimeException("Mapping failed")

        try {
            useCase().first()
            fail("Exception was expected but not thrown")
        } catch (e: RuntimeException) {
            assertEquals("Mapping failed", e.message)
        }

        coVerify(exactly = 1) { newsRepository.fetchFavoriteArticles() }
    }
}