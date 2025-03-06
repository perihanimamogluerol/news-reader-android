package com.perihan.newsreaderandroid.news.topheadlines

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
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.perihan.newsreaderandroid.UiState
import com.perihan.newsreaderandroid.domain.ArticleDomainModel
import com.perihan.newsreaderandroid.news.R
import com.perihan.newsreaderandroid.news.item.ArticleItem
import kotlinx.coroutines.flow.Flow

@Composable
fun NewsTopHeadlinesScreen(
    navController: NavController, viewModel: NewsTopHeadlinesViewModel = hiltViewModel()
) {
    val newsState by viewModel.newsState.collectAsState()

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            when (newsState) {
                is UiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is UiState.Success -> {
                    val newsList =
                        (newsState as UiState.Success<Flow<PagingData<ArticleDomainModel>>>).data.collectAsLazyPagingItems()
                    NewsList(newsList, navController)
                }

                is UiState.Error -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = (newsState as UiState.Error).message.orEmpty().ifBlank {
                            stringResource(R.string.error_text)
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun NewsList(newsList: LazyPagingItems<ArticleDomainModel>, navController: NavController) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        item {
            Text(
                text = stringResource(R.string.top_news_title),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
        }

        items(newsList.itemCount) { index ->
            newsList[index]?.let { article ->
                ArticleItem(navController = navController, article = article)
            }
        }
    }
}