package com.perihan.newsreaderandroid.news

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.perihan.newsreaderandroid.domain.ArticleDomainModel
import com.perihan.newsreaderandroid.domain.FetchTopHeadlinesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class NewsTopHeadlinesViewModel
@Inject constructor(
    fetchTopHeadlinesUseCase: FetchTopHeadlinesUseCase
) : ViewModel() {
    var selectedUrl by mutableStateOf<String?>(null)

    val newsFlow: Flow<PagingData<ArticleDomainModel>> =
        fetchTopHeadlinesUseCase.execute().cachedIn(viewModelScope)
}