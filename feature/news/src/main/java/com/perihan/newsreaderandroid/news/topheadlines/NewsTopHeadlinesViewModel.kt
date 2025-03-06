package com.perihan.newsreaderandroid.news.topheadlines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.perihan.newsreaderandroid.UiState
import com.perihan.newsreaderandroid.domain.ArticleDomainModel
import com.perihan.newsreaderandroid.domain.FetchTopHeadlinesUseCase
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
class NewsTopHeadlinesViewModel
@Inject constructor(
    private val fetchTopHeadlinesUseCase: FetchTopHeadlinesUseCase
) : ViewModel() {

    private val _newsState =
        MutableStateFlow<UiState<Flow<PagingData<ArticleDomainModel>>>>(UiState.Loading)
    val newsState: StateFlow<UiState<Flow<PagingData<ArticleDomainModel>>>> =
        _newsState.asStateFlow()

    init {
        fetchTopHeadlines()
    }

    private fun fetchTopHeadlines() {
        viewModelScope.launch {
            fetchTopHeadlinesUseCase.execute().cachedIn(viewModelScope).catch { exception ->
                _newsState.value = UiState.Error(exception.message)
            }.collectLatest { pagingData ->
                _newsState.value = UiState.Success(flowOf(pagingData))
            }
        }
    }
}