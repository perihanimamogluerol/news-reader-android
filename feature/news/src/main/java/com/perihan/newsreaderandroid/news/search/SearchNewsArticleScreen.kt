package com.perihan.newsreaderandroid.news.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.perihan.newsreaderandroid.core.common.UiState
import com.perihan.newsreaderandroid.domain.model.ArticleDomainModel
import com.perihan.newsreaderandroid.news.R
import com.perihan.newsreaderandroid.news.item.ArticleItem
import kotlinx.coroutines.flow.Flow

@Composable
fun SearchNewsArticleScreen(
    navController: NavController,
    viewModel: SearchNewsArticleViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.searchNewsArticle()
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
fun Init(navController: NavController, viewModel: SearchNewsArticleViewModel = hiltViewModel()) {
    val searchState by viewModel.searchNewsArticleState.collectAsState()
    val query by viewModel.query.collectAsState()

    TextField(value = query,
        onValueChange = { viewModel.setQuery(it) },
        placeholder = { Text(stringResource(R.string.search_news_placeholder)) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "") })

    when (searchState) {
        is UiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is UiState.Success -> {
            val response =
                (searchState as UiState.Success<Flow<PagingData<ArticleDomainModel>>>).data.collectAsLazyPagingItems()
            SearchNewsArticleContent(response, navController)
        }

        is UiState.Error -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = (searchState as UiState.Error).message.orEmpty().ifBlank {
                    stringResource(R.string.error_text)
                })
            }
        }
    }
}

@Composable
fun SearchNewsArticleContent(
    response: LazyPagingItems<ArticleDomainModel>,
    navController: NavController,
    viewModel: SearchNewsArticleViewModel = hiltViewModel()
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(response.itemCount) { index ->
            response[index]?.let { article ->
                ArticleItem(navController = navController,
                    article = article,
                    modifier = Modifier.animateItem(fadeInSpec = null, fadeOutSpec = null),
                    { isFavorite ->
                        viewModel.toggleFavorite(
                            article = article, isFavorite = isFavorite
                        )
                    })
            }
        }
    }
}