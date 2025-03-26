package com.perihan.newsreaderandroid.utils

import com.perihan.newsreaderandroid.domain.model.ArticleDomainModel

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