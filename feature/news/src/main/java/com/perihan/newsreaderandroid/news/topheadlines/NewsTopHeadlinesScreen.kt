package com.perihan.newsreaderandroid.news.topheadlines

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.perihan.newsreaderandroid.core.common.UiState
import com.perihan.newsreaderandroid.core.navigation.NewsNavKeys
import com.perihan.newsreaderandroid.core.navigation.NewsNavRoute
import com.perihan.newsreaderandroid.domain.model.ArticleDomainModel
import com.perihan.newsreaderandroid.domain.model.SourceDomainModel
import com.perihan.newsreaderandroid.news.R
import com.perihan.newsreaderandroid.news.item.ArticleItem
import kotlinx.coroutines.flow.Flow

@Composable
fun NewsTopHeadlinesScreen(
    navController: NavController, viewModel: NewsTopHeadlinesViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.fetchTopHeadlines()
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
fun Init(navController: NavController, viewModel: NewsTopHeadlinesViewModel = hiltViewModel()) {
    val state by viewModel.topHeadlinesState.collectAsState()

    when (state) {
        is UiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is UiState.Success -> {
            val response =
                (state as UiState.Success<Flow<PagingData<ArticleDomainModel>>>).data.collectAsLazyPagingItems()
            NewsContent(response, navController)
        }

        is UiState.Error -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = (state as UiState.Error).message.orEmpty()
                    .ifBlank { stringResource(R.string.error_text) })
            }
        }
    }
}

@Composable
fun NewsContent(
    response: LazyPagingItems<ArticleDomainModel>,
    navController: NavController,
    viewModel: NewsTopHeadlinesViewModel = hiltViewModel()
) {
    val sourcesState by viewModel.newsSourcesState.collectAsState()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        val sources = (sourcesState as? UiState.Success)?.data

        sources?.let {
            item { NewsListTitle(stringResource(R.string.sources), modifier = Modifier) }

            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(it) { source ->
                        SourceCard(source, navController)
                    }
                }
            }
        }

        item {
            NewsListTitle(
                stringResource(R.string.top_news_title), modifier = Modifier.padding(top = 32.dp)
            )
        }

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

@Composable
fun NewsListTitle(title: String, modifier: Modifier) {
    Text(
        text = title, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = modifier
    )
}

@Composable
fun SourceCard(source: SourceDomainModel, navController: NavController) {
    Card(
        modifier = Modifier
            .width(90.dp)
            .height(60.dp)
            .clickable {
                navController.currentBackStackEntry?.savedStateHandle?.set(
                    NewsNavKeys.SOURCE_ID, source.id
                )
                navController.navigate(NewsNavRoute.NewsArticles.route)
            }, elevation = CardDefaults.cardElevation(4.dp), shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = source.name,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}