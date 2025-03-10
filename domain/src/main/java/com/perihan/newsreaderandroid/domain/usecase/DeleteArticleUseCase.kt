package com.perihan.newsreaderandroid.domain.usecase

import com.perihan.newsreaderandroid.data.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteArticleUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    operator fun invoke(articleTitle: String): Flow<Unit> =
        newsRepository.deleteArticle(articleTitle = articleTitle)
}