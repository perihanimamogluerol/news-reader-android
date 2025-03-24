package com.perihan.newsreaderandroid.news.topheadlines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.perihan.newsreaderandroid.core.common.UiState
import com.perihan.newsreaderandroid.domain.model.ArticleDomainModel
import com.perihan.newsreaderandroid.domain.model.SourceDomainModel
import com.perihan.newsreaderandroid.domain.usecase.DeleteArticleUseCase
import com.perihan.newsreaderandroid.domain.usecase.FetchNewsSourcesUseCase
import com.perihan.newsreaderandroid.domain.usecase.FetchTopHeadlinesUseCase
import com.perihan.newsreaderandroid.domain.usecase.InsertArticleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsTopHeadlinesViewModel
@Inject constructor(
    fetchTopHeadlinesUseCase: FetchTopHeadlinesUseCase,
    private val fetchNewsSourcesUseCase: FetchNewsSourcesUseCase,
    private val insertArticleUseCase: InsertArticleUseCase,
    private val deleteArticleUseCase: DeleteArticleUseCase
) : ViewModel() {

    val topHeadlinesState: Flow<PagingData<ArticleDomainModel>> =
        fetchTopHeadlinesUseCase().cachedIn(viewModelScope)

    private val _newsSourcesState =
        MutableStateFlow<UiState<List<SourceDomainModel>>>(UiState.Loading)
    val newsSourcesState: StateFlow<UiState<List<SourceDomainModel>>> =
        _newsSourcesState.asStateFlow()

    init {
        fetchNewsSources()
    }

    private fun fetchNewsSources() {
        viewModelScope.launch {
            fetchNewsSourcesUseCase().catch { exception ->
                _newsSourcesState.value = UiState.Error(exception.message)
            }.collectLatest { response ->
                _newsSourcesState.value = UiState.Success(response)
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
}