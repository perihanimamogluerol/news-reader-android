package com.perihan.newsreaderandroid.domain.usecase

import com.perihan.newsreaderandroid.data.repository.NewsRepository
import com.perihan.newsreaderandroid.domain.mapper.ArticleDomainMapper
import com.perihan.newsreaderandroid.domain.model.ArticleDomainModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FetchFavoriteArticlesUseCase @Inject constructor(
    private val newsRepository: NewsRepository, private val articleDomainMapper: ArticleDomainMapper
) {
    operator fun invoke(): Flow<List<ArticleDomainModel>> = newsRepository.fetchFavoriteArticles()
        .map { favoriteArticles -> favoriteArticles.map { articleDomainMapper.toLocalDomain(it) } }
}