package com.perihan.newsreaderandroid.domain.usecase

import androidx.paging.PagingData
import androidx.paging.map
import com.perihan.newsreaderandroid.data.repository.NewsRepository
import com.perihan.newsreaderandroid.domain.mapper.ArticleDomainMapper
import com.perihan.newsreaderandroid.domain.model.ArticleDomainModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchNewsArticleUseCase @Inject constructor(
    private val newsRepository: NewsRepository, private val articleDomainMapper: ArticleDomainMapper
) {
    operator fun invoke(sources: String?, query: String): Flow<PagingData<ArticleDomainModel>> =
        newsRepository.searchNewsArticle(sources = sources, query = query)
            .map { pagingData -> pagingData.map { articleDomainMapper.toDomain(it) } }
}