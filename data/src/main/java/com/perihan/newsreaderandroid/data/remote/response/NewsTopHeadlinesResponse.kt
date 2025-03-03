package com.perihan.newsreaderandroid.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class NewsTopHeadlinesResponse(
    val status: String?,
    val totalResults: Int?,
    val articles: List<ArticleResponse>?
)