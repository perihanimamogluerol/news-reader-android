package com.perihan.newsreaderandroid.domain

import androidx.paging.PagingData
import androidx.paging.map
import com.perihan.newsreaderandroid.data.remote.repository.NewsRepository
import com.perihan.newsreaderandroid.domain.mapper.ArticleDomainMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FetchTopHeadlinesUseCase @Inject constructor(
    private val newsRepository: NewsRepository, private val articleDomainMapper: ArticleDomainMapper
) {
    fun execute(): Flow<PagingData<ArticleDomainModel>> = newsRepository.fetchTopHeadlines()
        .map { pagingData -> pagingData.map { articleDomainMapper.toDomain(it) } }
}