package com.perihan.newsreaderandroid.domain.usecase

import com.perihan.newsreaderandroid.data.repository.NewsRepository
import com.perihan.newsreaderandroid.domain.mapper.ArticleDomainMapper
import com.perihan.newsreaderandroid.domain.model.ArticleDomainModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class InsertArticleUseCase @Inject constructor(
    private val newsRepository: NewsRepository, private val articleDomainMapper: ArticleDomainMapper
) {
    operator fun invoke(article: ArticleDomainModel): Flow<Unit> =
        newsRepository.insertArticle(articleDomainMapper.toLocalData(input = article))
}