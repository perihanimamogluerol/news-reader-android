package com.perihan.newsreaderandroid.domain.mapper

import com.perihan.newsreaderandroid.core.common.BaseMapper
import com.perihan.newsreaderandroid.data.local.entity.ArticleEntity
import com.perihan.newsreaderandroid.data.remote.response.topheadline.ArticleResponse
import com.perihan.newsreaderandroid.domain.model.ArticleDomainModel
import javax.inject.Inject

class ArticleDomainMapper @Inject constructor() :
    BaseMapper<ArticleResponse, ArticleDomainModel, ArticleEntity> {

    override fun toDomain(input: ArticleResponse): ArticleDomainModel = with(input) {
        ArticleDomainModel(
            title = title.orEmpty(),
            description = description.orEmpty(),
            imageUrl = imageUrl.orEmpty(),
            url = url.orEmpty(),
            isFavorite = isFavorite
        )
    }

    override fun toLocalData(input: ArticleDomainModel): ArticleEntity = with(input) {
        ArticleEntity(
            title = title, imageUrl = imageUrl, url = url
        )
    }

    override fun toLocalDomain(input: ArticleEntity): ArticleDomainModel = with(input) {
        ArticleDomainModel(
            title = title, description = "", imageUrl = imageUrl, url = url, isFavorite = true
        )
    }
}
