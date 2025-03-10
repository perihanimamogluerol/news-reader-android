package com.perihan.newsreaderandroid.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class ArticleDomainModel(
    val title: String,
    val description: String,
    val imageUrl: String,
    val url: String,
    var isFavorite: Boolean
) : Parcelable