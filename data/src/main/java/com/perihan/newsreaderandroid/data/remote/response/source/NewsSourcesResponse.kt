package com.perihan.newsreaderandroid.data.remote.response.source

import kotlinx.serialization.Serializable

@Serializable
data class NewsSourcesResponse(
    val status: String?, val sources: List<SourceResponse>?
)