package com.perihan.newsreaderandroid.news.item

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.perihan.newsreaderandroid.domain.ArticleDomainModel
import com.perihan.newsreaderandroid.news.NewsNavKeys
import com.perihan.newsreaderandroid.news.NewsNavRoute

@Composable
fun ArticleItem(
    navController: NavController, article: ArticleDomainModel
) {
    Card(shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = {
            navController.currentBackStackEntry?.savedStateHandle?.set(NewsNavKeys.ARTICLE, article)
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