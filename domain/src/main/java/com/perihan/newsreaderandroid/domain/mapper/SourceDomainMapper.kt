package com.perihan.newsreaderandroid.domain.mapper

import com.perihan.newsreaderandroid.core.common.BaseMapper
import com.perihan.newsreaderandroid.data.remote.response.source.SourceResponse
import com.perihan.newsreaderandroid.domain.model.SourceDomainModel
import javax.inject.Inject

class SourceDomainMapper @Inject constructor() :
    BaseMapper<SourceResponse, SourceDomainModel, Nothing> {
    override fun toDomain(input: SourceResponse): SourceDomainModel = with(input) {
        SourceDomainModel(
            id = id.orEmpty(),
            name = name.orEmpty(),
            description = description.orEmpty(),
            url = url.orEmpty(),
            category = category.orEmpty()
        )
    }
}
