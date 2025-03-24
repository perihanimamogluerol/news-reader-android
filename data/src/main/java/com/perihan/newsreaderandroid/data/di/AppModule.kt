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
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindNewsRemoteDataSource(remoteDataSource: NewsRemoteDataSourceImpl): NewsRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindNewsRepository(repository: NewsRepositoryImpl): NewsRepository

    @Binds
    @Singleton
    abstract fun bindNewsLocalDataSource(localDataSource: NewsLocalDataSourceImpl): NewsLocalDataSource
}