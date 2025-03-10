package com.perihan.newsreaderandroid.data.di

import com.perihan.newsreaderandroid.data.local.NewsLocalDataSource
import com.perihan.newsreaderandroid.data.local.NewsLocalDataSourceImpl
import com.perihan.newsreaderandroid.data.remote.NewsRemoteDataSource
import com.perihan.newsreaderandroid.data.remote.NewsRemoteDataSourceImpl
import com.perihan.newsreaderandroid.data.repository.NewsRepository
import com.perihan.newsreaderandroid.data.repository.NewsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class AppModule {

    @Binds
    @ViewModelScoped
    abstract fun bindNewsRemoteDataSource(remoteDataSource: NewsRemoteDataSourceImpl): NewsRemoteDataSource

    @Binds
    @ViewModelScoped
    abstract fun bindNewsRepository(repository: NewsRepositoryImpl): NewsRepository

    @Binds
    @ViewModelScoped
    abstract fun bindNewsLocalDataSource(localDataSource: NewsLocalDataSourceImpl): NewsLocalDataSource
}