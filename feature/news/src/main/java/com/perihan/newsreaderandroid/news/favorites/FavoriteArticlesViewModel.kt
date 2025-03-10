package com.perihan.newsreaderandroid.news.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.perihan.newsreaderandroid.core.common.UiState
import com.perihan.newsreaderandroid.domain.model.ArticleDomainModel
import com.perihan.newsreaderandroid.domain.usecase.DeleteArticleUseCase
import com.perihan.newsreaderandroid.domain.usecase.FetchFavoriteArticlesUseCase
import com.perihan.newsreaderandroid.domain.usecase.InsertArticleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteArticlesViewModel
@Inject constructor(
    private val fetchFavoriteArticlesUseCase: FetchFavoriteArticlesUseCase,
    private val insertArticleUseCase: InsertArticleUseCase,
    private val deleteArticleUseCase: DeleteArticleUseCase
) : ViewModel() {

    private val _favoriteArticlesState =
        MutableStateFlow<UiState<List<ArticleDomainModel>>>(UiState.Loading)
    val favoriteArticlesState: StateFlow<UiState<List<ArticleDomainModel>>> =
        _favoriteArticlesState.asStateFlow()

    fun fetchFavorites() {
        viewModelScope.launch {
            fetchFavoriteArticlesUseCase().catch { exception ->
                _favoriteArticlesState.value = UiState.Error(exception.message)
            }.collectLatest { response ->
                _favoriteArticlesState.value = UiState.Success(response)
            }
        }
    }

    fun toggleFavorite(article: ArticleDomainModel, isFavorite: Boolean) {
        viewModelScope.launch {
            if (isFavorite) {
                deleteArticleUseCase(article.title).collect {
                    removeFavoriteItem(article = article)
                }
            } else {
                insertArticleUseCase(article).collect {}
            }
        }
    }

    private fun removeFavoriteItem(article: ArticleDomainModel) {
        val currentState = _favoriteArticlesState.value
        if (currentState is UiState.Success) {
            val updatedList = currentState.data - article
            _favoriteArticlesState.value = UiState.Success(updatedList)
        }
    }
}