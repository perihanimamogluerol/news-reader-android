package com.perihan.newsreaderandroid.news

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.perihan.newsreaderandroid.core.navigation.NewsNavRoute
import com.perihan.newsreaderandroid.news.favorites.FavoriteArticlesScreen
import com.perihan.newsreaderandroid.news.newsarticles.NewsArticlesScreen
import com.perihan.newsreaderandroid.news.newsdetail.NewsDetailScreen
import com.perihan.newsreaderandroid.news.search.SearchNewsArticleScreen
import com.perihan.newsreaderandroid.news.topheadlines.NewsTopHeadlinesScreen

@Composable
fun NewsNavGraph() {
    val navController = rememberNavController()

    Scaffold(bottomBar = { BottomNavigationBar(navController) }) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = NewsNavRoute.NewsTopHeadlines.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(NewsNavRoute.NewsTopHeadlines.route) { NewsTopHeadlinesScreen(navController) }
            composable(NewsNavRoute.NewsSearch.route) { SearchNewsArticleScreen(navController) }
            composable(NewsNavRoute.NewsFavorites.route) { FavoriteArticlesScreen(navController) }
            composable(NewsNavRoute.NewsDetail.route) { NewsDetailScreen(navController) }
            composable(NewsNavRoute.NewsArticles.route) { NewsArticlesScreen(navController) }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        NewsNavRoute.NewsTopHeadlines, NewsNavRoute.NewsSearch, NewsNavRoute.NewsFavorites
    )

    NavigationBar {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        items.forEach { screen ->
            NavigationBarItem(icon = { Icon(screen.icon, contentDescription = screen.title) },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                })
        }
    }
}