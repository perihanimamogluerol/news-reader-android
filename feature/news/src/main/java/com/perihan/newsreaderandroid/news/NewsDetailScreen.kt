package com.perihan.newsreaderandroid.news

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.perihan.newsreaderandroid.domain.ArticleDomainModel

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun NewsDetailScreen(navController: NavController) {
    val article =
        navController.previousBackStackEntry?.savedStateHandle?.get<ArticleDomainModel>("article")

    val context = LocalContext.current

    val webView = remember {
        WebView(context).apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            webViewClient = WebViewClient()
            loadUrl(article?.url ?: "")
        }
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            AndroidView(factory = { webView })
        }
    }
}