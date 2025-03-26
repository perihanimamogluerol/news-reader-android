package com.perihan.newsreaderandroid.news

import com.perihan.newsreaderandroid.domain.usecase.DeleteArticleUseCase
import com.perihan.newsreaderandroid.domain.usecase.InsertArticleUseCase
import com.perihan.newsreaderandroid.domain.usecase.SearchNewsArticleUseCase
import com.perihan.newsreaderandroid.news.search.SearchNewsArticleViewModel
import com.perihan.newsreaderandroid.utils.articleDomainModel
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchNewsArticleViewModelTest {

    private val searchNewsArticleUseCase = mockk<SearchNewsArticleUseCase>(relaxed = true)
    private val insertArticleUseCase = mockk<InsertArticleUseCase>()
    private val deleteArticleUseCase = mockk<DeleteArticleUseCase>()

    private lateinit var viewModel: SearchNewsArticleViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        viewModel = SearchNewsArticleViewModel(
            searchNewsArticleUseCase, insertArticleUseCase, deleteArticleUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN isFavorite true WHEN toggleFavorite called THEN deleteArticleUseCase is called`() =
        runTest {
            val article = articleDomainModel("News A")
            every { deleteArticleUseCase(article.title) } returns flowOf(Unit)
            every { insertArticleUseCase(article) } returns flowOf(Unit)

            viewModel.toggleFavorite(article, isFavorite = true)
            advanceUntilIdle()

            coVerify(exactly = 1) { deleteArticleUseCase(article.title) }
            coVerify(exactly = 0) { insertArticleUseCase(article) }
        }

    @Test
    fun `GIVEN isFavorite false WHEN toggleFavorite called THEN insertArticleUseCase is called`() =
        runTest {
            val article = articleDomainModel("News A")
            every { deleteArticleUseCase(article.title) } returns flowOf(Unit)
            every { insertArticleUseCase(article) } returns flowOf(Unit)

            viewModel.toggleFavorite(article, isFavorite = false)
            advanceUntilIdle()

            coVerify(exactly = 1) { insertArticleUseCase(article) }
            coVerify(exactly = 0) { deleteArticleUseCase(article.title) }
        }
}