package com.perihan.newsreaderandroid.data.remote.pagingsource

import androidx.paging.PagingSource

interface PagingSourceFactory<T : Any> {
    fun create(): PagingSource<Int, T>
}