package com.perihan.newsreaderandroid.news.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.perihan.newsreaderandroid.core.common.UiState
import com.perihan.newsreaderandroid.domain.model.ArticleDomainModel
import com.perihan.newsreaderandroid.news.R
import com.perihan.newsreaderandroid.news.item.ArticleItem

@Composable
fun FavoriteArticlesScreen(
    navController: NavController, viewModel: FavoriteArticlesViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.fetchFavorites()
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            Init(navController = navController)
        }
    }
}

@Composable
fun Init(navController: NavController, viewModel: FavoriteArticlesViewModel = hiltViewModel()) {
    val favoriteArticlesState by viewModel.favoriteArticlesState.collectAsState()

    when (favoriteArticlesState) {
        is UiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is UiState.Success -> {
            val response: List<ArticleDomainModel> =
                (favoriteArticlesState as UiState.Success<List<ArticleDomainModel>>).data
            FavoriteArticlesContent(response = response, navController = navController)
        }

        is UiState.Error -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = (favoriteArticlesState as UiState.Error).message.orEmpty()
                        .ifBlank { stringResource(R.string.error_text) })
            }
        }
    }
}

@Composable
fun FavoriteArticlesContent(
    response: List<ArticleDomainModel>,
    navController: NavController,
    viewModel: FavoriteArticlesViewModel = hiltViewModel()
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        item {
            Text(
                text = stringResource(R.string.my_favorites),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        items(response, key = { it.title }) { article ->
            ArticleItem(
                navController = navController,
                article = article,
                modifier = Modifier.animateItem(fadeInSpec = null, fadeOutSpec = null),
                { isFavorite ->
                    viewModel.toggleFavorite(article = article, isFavorite = isFavorite)
                }
            )
        }
    }
}