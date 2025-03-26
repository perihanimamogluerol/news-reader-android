package com.perihan.newsreaderandroid.domain

import com.perihan.newsreaderandroid.data.repository.NewsRepository
import com.perihan.newsreaderandroid.domain.usecase.DeleteArticleUseCase
import io.mockk.coEvery
import io.mockk.coVerify
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
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DeleteArticleUseCaseTest {

    private val newsRepository: NewsRepository = mockk()
    private lateinit var useCase: DeleteArticleUseCase

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        useCase = DeleteArticleUseCase(newsRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN article title WHEN invoking use case THEN repository is called with title`() =
        runTest {
            val title = "News A"
            coEvery { newsRepository.deleteArticle(title) } returns flowOf(Unit)

            val result = useCase(title).first()

            assertEquals(Unit, result)
            coVerify(exactly = 1) { newsRepository.deleteArticle(title) }
        }

    @Test
    fun `GIVEN repository throws exception WHEN deleting THEN propagates exception`() = runTest {
        val title = "News A"
        coEvery { newsRepository.deleteArticle(title) } throws RuntimeException("Delete failed")

        try {
            useCase(title).first()
            fail("Exception was expected but not thrown")
        } catch (e: RuntimeException) {
            assertEquals("Delete failed", e.message)
        }

        coVerify(exactly = 1) { newsRepository.deleteArticle(title) }
    }
}
