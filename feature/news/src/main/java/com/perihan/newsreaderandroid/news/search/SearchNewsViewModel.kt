package com.perihan.newsreaderandroid.news.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.perihan.newsreaderandroid.UiState
import com.perihan.newsreaderandroid.domain.ArticleDomainModel
import com.perihan.newsreaderandroid.domain.SearchNewsUseCase
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
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchNewsViewModel
@Inject constructor(
    private val searchNewsUseCase: SearchNewsUseCase
) : ViewModel() {

    private val _query = MutableStateFlow("news")
    val query: StateFlow<String> = _query

    private val _searchNewsState =
        MutableStateFlow<UiState<Flow<PagingData<ArticleDomainModel>>>>(UiState.Loading)
    val searchNewsState: StateFlow<UiState<Flow<PagingData<ArticleDomainModel>>>> =
        _searchNewsState.asStateFlow()

    init {
        searchNews()
    }

    private fun searchNews() {
        viewModelScope.launch {
            query.debounce(500).distinctUntilChanged().flatMapLatest { query ->
                    searchNewsUseCase.execute(query = query).cachedIn(viewModelScope)
                        .onStart { _searchNewsState.value = UiState.Loading }.catch { exception ->
                            _searchNewsState.value = UiState.Error(exception.message)
                        }
                }.collectLatest { pagingData ->
                    _searchNewsState.value = UiState.Success(flowOf(pagingData))
                }
        }
    }

    fun setQuery(newQuery: String) {
        _query.value = newQuery
    }
}