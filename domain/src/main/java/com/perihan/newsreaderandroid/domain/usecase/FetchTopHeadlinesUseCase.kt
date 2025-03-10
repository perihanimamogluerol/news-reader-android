package com.perihan.newsreaderandroid.domain.usecase

import androidx.paging.PagingData
import androidx.paging.map
import com.perihan.newsreaderandroid.data.repository.NewsRepository
import com.perihan.newsreaderandroid.domain.mapper.ArticleDomainMapper
import com.perihan.newsreaderandroid.domain.model.ArticleDomainModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FetchTopHeadlinesUseCase @Inject constructor(
    private val newsRepository: NewsRepository, private val articleDomainMapper: ArticleDomainMapper
) {
    operator fun invoke(): Flow<PagingData<ArticleDomainModel>> = newsRepository.fetchTopHeadlines()
        .map { pagingData -> pagingData.map { articleDomainMapper.toDomain(it) } }
}