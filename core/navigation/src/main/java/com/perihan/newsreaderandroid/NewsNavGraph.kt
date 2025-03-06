package com.perihan.newsreaderandroid

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
import com.perihan.newsreaderandroid.news.favorites.FavoritesScreen
import com.perihan.newsreaderandroid.news.newsdetail.NewsDetailScreen
import com.perihan.newsreaderandroid.news.NewsNavRoute
import com.perihan.newsreaderandroid.news.topheadlines.NewsTopHeadlinesScreen
import com.perihan.newsreaderandroid.news.search.SearchScreen

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
            composable(NewsNavRoute.NewsSearch.route) { SearchScreen(navController) }
            composable(NewsNavRoute.NewsFavorites.route) { FavoritesScreen() }
            composable(NewsNavRoute.NewsDetail.route) { NewsDetailScreen(navController) }
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
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.title) },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}