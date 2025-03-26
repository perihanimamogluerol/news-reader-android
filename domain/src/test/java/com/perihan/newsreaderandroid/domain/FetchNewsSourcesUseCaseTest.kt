package com.perihan.newsreaderandroid.domain

import com.perihan.newsreaderandroid.data.repository.NewsRepository
import com.perihan.newsreaderandroid.domain.mapper.SourceDomainMapper
import com.perihan.newsreaderandroid.domain.usecase.FetchNewsSourcesUseCase
import com.perihan.newsreaderandroid.domain.utils.newsSourcesResponse
import com.perihan.newsreaderandroid.domain.utils.sourceDomainModel
import com.perihan.newsreaderandroid.domain.utils.sourceResponse
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
class FetchNewsSourcesUseCaseTest {

    private val newsRepository: NewsRepository = mockk()
    private val sourceDomainMapper: SourceDomainMapper = mockk()
    private lateinit var useCase: FetchNewsSourcesUseCase

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        useCase = FetchNewsSourcesUseCase(newsRepository, sourceDomainMapper)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN valid sources WHEN fetching THEN returns mapped list`() = runTest {
        val sourceResponse = sourceResponse("cnn")
        val newsSourcesResponse = newsSourcesResponse(sources = listOf(sourceResponse))

        coEvery { newsRepository.fetchNewsSources() } returns flowOf(newsSourcesResponse)

        val sourceDomainModel = sourceDomainModel("cnn")
        every { sourceDomainMapper.toDomain(sourceResponse) } returns sourceDomainModel

        val result = useCase().first()

        assertEquals(1, result.size)
        assertEquals(sourceDomainModel, result[0])

        coVerify(exactly = 1) { newsRepository.fetchNewsSources() }
    }

    @Test
    fun `GIVEN null sources WHEN fetching THEN returns empty list`() = runTest {
        val newsSourcesResponse = newsSourcesResponse(sources = null)
        coEvery { newsRepository.fetchNewsSources() } returns flowOf(newsSourcesResponse)

        val result = useCase().first()

        assertTrue(result.isEmpty())
        coVerify(exactly = 1) { newsRepository.fetchNewsSources() }
    }

    @Test
    fun `GIVEN repository throws exception WHEN fetching THEN propagates exception`() = runTest {
        coEvery { newsRepository.fetchNewsSources() } throws RuntimeException("Network error")

        try {
            useCase().first()
            fail("Exception was expected but not thrown")
        } catch (e: RuntimeException) {
            assertEquals("Network error", e.message)
        }

        coVerify(exactly = 1) { newsRepository.fetchNewsSources() }
    }

    @Test
    fun `GIVEN multiple sources WHEN fetching THEN maps all correctly`() = runTest {
        val response1 = sourceResponse("cnn")
        val response2 = sourceResponse("bbc")
        val newsSourcesResponse = newsSourcesResponse(sources = listOf(response1, response2))

        coEvery { newsRepository.fetchNewsSources() } returns flowOf(newsSourcesResponse)

        val domain1 = sourceDomainModel("cnn")
        val domain2 = sourceDomainModel("bbc")

        every { sourceDomainMapper.toDomain(response1) } returns domain1
        every { sourceDomainMapper.toDomain(response2) } returns domain2

        val result = useCase().first()

        assertEquals(2, result.size)
        assertEquals(listOf(domain1, domain2), result)
    }
}