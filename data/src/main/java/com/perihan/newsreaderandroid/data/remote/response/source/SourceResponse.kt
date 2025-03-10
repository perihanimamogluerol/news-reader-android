package com.perihan.newsreaderandroid.data.remote.response.source

import kotlinx.serialization.Serializable

@Serializable
data class SourceResponse(
    val id: String?,
    val name: String?,
    val description: String?,
    val url: String?,
    val category: String?
)