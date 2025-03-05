package com.perihan.newsreaderandroid.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class ArticleDomainModel(
    var title: String, var description: String, var imageUrl: String, var url: String
) : Parcelable