package com.perihan.newsreaderandroid.domain.mapper

import com.perihan.newsreaderandroid.data.remote.response.ArticleResponse
import com.perihan.newsreaderandroid.domain.ArticleDomainModel
import javax.inject.Inject

class ArticleDomainMapper @Inject constructor() : BaseMapper<ArticleResponse, ArticleDomainModel> {
    override fun toDomain(input: ArticleResponse) = ArticleDomainModel(
        title = input.title.orEmpty(),
        description = input.description.orEmpty(),
        imageUrl = input.imageUrl.orEmpty(),
        url = input.url.orEmpty()
    )
}