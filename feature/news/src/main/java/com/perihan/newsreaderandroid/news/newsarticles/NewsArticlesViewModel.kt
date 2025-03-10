package com.perihan.newsreaderandroid.news.newsarticles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.perihan.newsreaderandroid.core.common.UiState
import com.perihan.newsreaderandroid.domain.model.ArticleDomainModel
import com.perihan.newsreaderandroid.domain.usecase.DeleteArticleUseCase
import com.perihan.newsreaderandroid.domain.usecase.InsertArticleUseCase
import com.perihan.newsreaderandroid.domain.usecase.SearchNewsArticleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsArticlesViewModel
@Inject constructor(
    private val searchNewsArticleUseCase: SearchNewsArticleUseCase,
    private val insertArticleUseCase: InsertArticleUseCase,
    private val deleteArticleUseCase: DeleteArticleUseCase
) : ViewModel() {

    private val _searchNewsArticleState =
        MutableStateFlow<UiState<Flow<PagingData<ArticleDomainModel>>>>(UiState.Loading)
    val searchNewsArticleState: StateFlow<UiState<Flow<PagingData<ArticleDomainModel>>>> =
        _searchNewsArticleState.asStateFlow()

    fun searchNewsArticle(sources: String?) {
        viewModelScope.launch {
            searchNewsArticleUseCase(sources = sources, query = "news").cachedIn(viewModelScope)
                .catch { exception ->
                    _searchNewsArticleState.value = UiState.Error(exception.message)
                }.collectLatest { response ->
                    _searchNewsArticleState.value = UiState.Success(flowOf(response))
                }
        }
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