package com.perihan.newsreaderandroid.domain

import com.perihan.newsreaderandroid.data.repository.NewsRepository
import com.perihan.newsreaderandroid.domain.mapper.ArticleDomainMapper
import com.perihan.newsreaderandroid.domain.usecase.InsertArticleUseCase
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
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class InsertArticleUseCaseTest {

    private val newsRepository: NewsRepository = mockk()
    private val articleDomainMapper: ArticleDomainMapper = mockk()
    private lateinit var useCase: InsertArticleUseCase

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        useCase = InsertArticleUseCase(newsRepository, articleDomainMapper)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN valid ArticleDomainModel WHEN invoking use case THEN calls repository with mapped data`() =
        runTest {
            val articleDomainModel = articleDomainModel("News A")
            val articleEntity = articleEntity("News A")

            every { articleDomainMapper.toLocalData(articleDomainModel) } returns articleEntity
            coEvery { newsRepository.insertArticle(articleEntity) } returns flowOf(Unit)

            val result = useCase(articleDomainModel).first()

            assertEquals(Unit, result)
            coVerify(exactly = 1) { newsRepository.insertArticle(articleEntity) }
        }

    @Test
    fun `GIVEN mapper throws exception WHEN invoking use case THEN propagates exception`() =
        runTest {
            val articleDomainModel = articleDomainModel("News A")
            every { articleDomainMapper.toLocalData(articleDomainModel) } throws RuntimeException("Mapping failed")

            try {
                useCase(articleDomainModel).first()
                fail("Exception was expected but not thrown")
            } catch (e: RuntimeException) {
                assertEquals("Mapping failed", e.message)
            }
        }

    @Test
    fun `GIVEN repository throws exception WHEN inserting THEN propagates exception`() = runTest {
        val articleDomainModel = articleDomainModel("News A")
        val articleEntity = articleEntity("News A")

        every { articleDomainMapper.toLocalData(articleDomainModel) } returns articleEntity
        coEvery { newsRepository.insertArticle(articleEntity) } throws RuntimeException("DB insert error")

        try {
            useCase(articleDomainModel).first()
            fail("Exception was expected but not thrown")
        } catch (e: RuntimeException) {
            assertEquals("DB insert error", e.message)
        }

        coVerify(exactly = 1) { newsRepository.insertArticle(articleEntity) }
    }
}