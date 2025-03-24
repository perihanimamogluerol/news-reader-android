package com.perihan.newsreaderandroid.news.newsarticles

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.perihan.newsreaderandroid.core.navigation.NewsNavKeys
import com.perihan.newsreaderandroid.domain.model.ArticleDomainModel
import com.perihan.newsreaderandroid.news.R
import com.perihan.newsreaderandroid.news.item.ArticleItem

@Composable
fun NewsArticlesScreen(
    navController: NavController, viewModel: NewsArticlesViewModel = hiltViewModel()
) {
    val sourceId =
        navController.previousBackStackEntry?.savedStateHandle?.get<String>(NewsNavKeys.SOURCE_ID)

    LaunchedEffect(sourceId) {
        viewModel.searchNewsArticle(sourceId)
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            Init(navController = navController, sourceId = sourceId)
        }
    }
}

@Composable
fun Init(
    navController: NavController,
    sourceId: String?,
    viewModel: NewsArticlesViewModel = hiltViewModel()
) {
    val pagingItems = viewModel.searchNewsArticleState.collectAsLazyPagingItems()

    when (pagingItems.loadState.refresh) {
        is LoadState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is LoadState.Error -> {
            val error = pagingItems.loadState.refresh as LoadState.Error
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = error.error.localizedMessage.orEmpty().ifBlank {
                    stringResource(R.string.error_text)
                })
            }
        }

        else -> {
            NewsArticlesContent(sourceId, pagingItems, navController)
        }
    }
}

@Composable
fun NewsArticlesContent(
    sourceId: String?,
    response: LazyPagingItems<ArticleDomainModel>,
    navController: NavController,
    viewModel: NewsArticlesViewModel = hiltViewModel()
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        item {
            Text(
                text = viewModel.formatSourceName(sourceId.orEmpty()),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        items(response.itemCount) { index ->
            response[index]?.let { article ->
                ArticleItem(navController = navController,
                    article = article,
                    modifier = Modifier.animateItem(fadeInSpec = null, fadeOutSpec = null),
                    { isFavorite ->
                        viewModel.toggleFavorite(article = article, isFavorite = isFavorite)
                    })
            }
        }
    }
}