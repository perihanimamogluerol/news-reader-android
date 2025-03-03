package com.perihan.newsreaderandroid

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.perihan.newsreaderandroid.news.NewsDetailScreen
import com.perihan.newsreaderandroid.news.NewsNavRoute
import com.perihan.newsreaderandroid.news.NewsTopHeadlinesScreen
import com.perihan.newsreaderandroid.news.NewsTopHeadlinesViewModel

@Composable
fun NewsNavHost(navController: NavHostController) {
    val viewModel: NewsTopHeadlinesViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = NewsNavRoute.NewsTopHeadlines.route) {
        composable(NewsNavRoute.NewsTopHeadlines.route) {
            NewsTopHeadlinesScreen(navController, viewModel)
        }
        composable(NewsNavRoute.NewsDetail.route) {
            NewsDetailScreen(viewModel)
        }
    }
}