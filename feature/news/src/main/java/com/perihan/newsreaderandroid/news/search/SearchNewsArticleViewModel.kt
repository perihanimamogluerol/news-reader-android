package com.perihan.newsreaderandroid.news.search

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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchNewsArticleViewModel
@Inject constructor(
    private val searchNewsArticleUseCase: SearchNewsArticleUseCase,
    private val insertArticleUseCase: InsertArticleUseCase,
    private val deleteArticleUseCase: DeleteArticleUseCase
) : ViewModel() {

    private val _query = MutableStateFlow("news")
    val query: StateFlow<String> = _query

    private val _searchNewsArticleState =
        MutableStateFlow<UiState<Flow<PagingData<ArticleDomainModel>>>>(UiState.Loading)
    val searchNewsArticleState: StateFlow<UiState<Flow<PagingData<ArticleDomainModel>>>> =
        _searchNewsArticleState.asStateFlow()

    fun searchNewsArticle() {
        viewModelScope.launch {
            query.debounce(500).distinctUntilChanged().flatMapLatest { query ->
                searchNewsArticleUseCase(sources = null, query = query).cachedIn(viewModelScope)
                    .catch { exception ->
                        _searchNewsArticleState.value = UiState.Error(exception.message)
                    }
            }.collectLatest { response ->
                _searchNewsArticleState.value = UiState.Success(flowOf(response))
            }
        }
    }

    fun setQuery(newQuery: String) {
        _query.value = newQuery
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
}