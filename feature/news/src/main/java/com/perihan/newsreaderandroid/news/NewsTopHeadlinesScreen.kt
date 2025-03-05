package com.perihan.newsreaderandroid.news

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.perihan.newsreaderandroid.domain.ArticleDomainModel

@Composable
fun NewsTopHeadlinesScreen(
    navController: NavController, viewModel: NewsTopHeadlinesViewModel = hiltViewModel()
) {
    val newsItems = viewModel.newsFlow.collectAsLazyPagingItems()

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                item {
                    Text(
                        text = "Top News",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                items(newsItems.itemCount) { index ->
                    newsItems[index]?.let { article ->
                        ArticleItem(
                            navController = navController, article = article, viewModel = viewModel
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ArticleItem(
    navController: NavController, article: ArticleDomainModel, viewModel: NewsTopHeadlinesViewModel
) {
    Card(shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = {
            navController.currentBackStackEntry?.savedStateHandle?.set("article", article)
            navController.navigate(NewsNavRoute.NewsDetail.route)
        }) {
        Column(modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                model = article.imageUrl,
                contentScale = ContentScale.Crop,
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(2.dp))
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = article.title,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = article.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}