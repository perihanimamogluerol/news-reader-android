package com.perihan.newsreaderandroid.news

sealed class NewsNavRoute(val route: String) {
    data object NewsTopHeadlines : NewsNavRoute("newsTopHeadlines")
    data object NewsDetail : NewsNavRoute("newsDetail")
}