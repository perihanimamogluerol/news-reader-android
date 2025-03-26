package com.perihan.newsreaderandroid.data.utils

import com.perihan.newsreaderandroid.data.local.entity.ArticleEntity
import com.perihan.newsreaderandroid.data.remote.response.source.NewsSourcesResponse
import com.perihan.newsreaderandroid.data.remote.response.source.SourceResponse
import com.perihan.newsreaderandroid.data.remote.response.topheadline.ArticleResponse

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

fun articleEntity(
    title: String = "Default Title", imageUrl: String = "", url: String = ""
) = ArticleEntity(
    title = title, imageUrl = imageUrl, url = url
)

fun remoteArticles(vararg titles: String): List<ArticleResponse> =
    titles.map { articleResponse(title = it) }

fun favoriteArticles(vararg titles: String): List<ArticleEntity> =
    titles.map { articleEntity(title = it) }

fun sourceResponse(
    id: String = "bbc-news",
    name: String = "BBC News",
    description: String = "",
    url: String = "",
    category: String = ""
) = SourceResponse(
    id = id, name = name, description = description, url = url, category = category
)

fun remoteSources(vararg names: String): List<SourceResponse> =
    names.map { sourceResponse(name = it) }

fun remoteNewsSources(sources: List<SourceResponse>) = NewsSourcesResponse(
    "ok", sources
)