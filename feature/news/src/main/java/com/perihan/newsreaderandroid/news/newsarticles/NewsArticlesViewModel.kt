package com.perihan.newsreaderandroid.news.newsarticles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.perihan.newsreaderandroid.domain.model.ArticleDomainModel
import com.perihan.newsreaderandroid.domain.usecase.DeleteArticleUseCase
import com.perihan.newsreaderandroid.domain.usecase.InsertArticleUseCase
import com.perihan.newsreaderandroid.domain.usecase.SearchNewsArticleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsArticlesViewModel
@Inject constructor(
    private val searchNewsArticleUseCase: SearchNewsArticleUseCase,
    private val insertArticleUseCase: InsertArticleUseCase,
    private val deleteArticleUseCase: DeleteArticleUseCase
) : ViewModel() {

    private val selectedSources = MutableStateFlow<String?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchNewsArticleState: Flow<PagingData<ArticleDomainModel>> =
        selectedSources.flatMapLatest { sources ->
            searchNewsArticleUseCase(sources = sources, query = "news")
        }.cachedIn(viewModelScope)

    fun searchNewsArticle(sources: String?) {
        selectedSources.value = sources
    }

    fun toggleFavorite(article: ArticleDomainModel, isFavorite: Boolean) {
        viewModelScope.launch {
            if (isFavorite) {
                deleteArticleUseCase(article.title).collect {}
            } else {
                insertArticleUseCase(article).collect {}
            }
        }
    }

    fun formatSourceName(sourceId: String): String {
        return sourceId.split("-")
            .joinToString(" ") { it.replaceFirstChar { char -> char.uppercaseChar() } }
    }
}