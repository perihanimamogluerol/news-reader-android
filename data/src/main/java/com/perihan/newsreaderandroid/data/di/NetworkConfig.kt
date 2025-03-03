package com.perihan.newsreaderandroid.data.di

import com.perihan.newsreaderandroid.data.BuildConfig

object NetworkConfig {
    const val AUTHORIZATION_HEADER = "Authorization"
    const val BEARER_TOKEN = "Bearer ${BuildConfig.API_KEY}"
    const val CONTENT_TYPE_JSON = "application/json"
}