package com.perihan.newsreaderandroid.data.remote.response.topheadline

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArticleResponse(
    val title: String?,
    val description: String?,
    @SerialName("urlToImage") val imageUrl: String?,
    val url: String?,
    val isFavorite: Boolean = false
)