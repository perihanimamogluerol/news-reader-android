package com.perihan.newsreaderandroid.domain.usecase

import com.perihan.newsreaderandroid.data.repository.NewsRepository
import com.perihan.newsreaderandroid.domain.mapper.SourceDomainMapper
import com.perihan.newsreaderandroid.domain.model.SourceDomainModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FetchNewsSourcesUseCase @Inject constructor(
    private val newsRepository: NewsRepository, private val sourceDomainMapper: SourceDomainMapper
) {
    operator fun invoke(): Flow<List<SourceDomainModel>> = newsRepository.fetchNewsSources()
        .map { newsSources ->
            newsSources.sources?.map { sourceDomainMapper.toDomain(it) } ?: emptyList()
        }
}