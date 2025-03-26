package com.perihan.newsreaderandroid.domain.utils

import com.perihan.newsreaderandroid.data.local.entity.ArticleEntity
import com.perihan.newsreaderandroid.data.remote.response.source.NewsSourcesResponse
import com.perihan.newsreaderandroid.data.remote.response.source.SourceResponse
import com.perihan.newsreaderandroid.data.remote.response.topheadline.ArticleResponse
import com.perihan.newsreaderandroid.domain.model.ArticleDomainModel
import com.perihan.newsreaderandroid.domain.model.SourceDomainModel

fun articleResponse(
    title: String = "Default Title",
    description: String = "",
    imageUrl: String = "",
    url: String = "",
    isFavorite: Boolean = false
) = ArticleResponse(
    title = title,
    description = description,
    imageUrl = imageUrl,
    url = url,
    isFavorite = isFavorite
)

fun articleDomainModel(
    title: String = "Default Title",
    description: String = "",
    imageUrl: String = "",
    url: String = "",
    isFavorite: Boolean = false
) = ArticleDomainModel(
    title = title,
    description = description,
    imageUrl = imageUrl,
    url = url,
    isFavorite = isFavorite
)

fun sourceResponse(
    id: String = "",
    name: String = "Default Title",
    description: String = "",
    url: String = "",
    category: String = ""
) = SourceResponse(
    id = id, name = name, description = description, url = url, category = category
)

fun newsSourcesResponse(status: String = "", sources: List<SourceResponse>?) =
    NewsSourcesResponse(status = status, sources = sources)

fun sourceDomainModel(
    id: String = "",
    name: String = "Default Title",
    description: String = "",
    url: String = "",
    category: String = ""
) = SourceDomainModel(
    id = id, name = name, description = description, url = url, category = category
)

fun articleEntity(
    title: String = "Default Title", imageUrl: String = "", url: String = ""
) = ArticleEntity(
    title = title, imageUrl = imageUrl, url = url
)