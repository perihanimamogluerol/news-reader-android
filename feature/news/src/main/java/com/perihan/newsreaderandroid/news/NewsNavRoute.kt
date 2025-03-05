package com.perihan.newsreaderandroid.news

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NewsNavRoute(val route: String, val title: String, val icon: ImageVector) {
    data object NewsTopHeadlines : NewsNavRoute("newsTopHeadlines", "", Icons.Default.Home)
    data object NewsSearch : NewsNavRoute("newsSearch", "", Icons.Default.Search)
    data object NewsFavorites : NewsNavRoute("newsFavorites", "", Icons.Default.Favorite)
    data object NewsDetail : NewsNavRoute("newsDetail", "", Icons.Default.Menu)
}