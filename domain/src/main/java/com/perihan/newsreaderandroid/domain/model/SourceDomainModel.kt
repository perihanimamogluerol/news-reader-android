package com.perihan.newsreaderandroid.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class SourceDomainModel(
    val id: String, val name: String, val description: String, val url: String, val category: String
) : Parcelable