package com.github.cleancompose.data.di

import com.github.cleancompose.data.repository.ConnectivityRepositoryImpl
import com.github.cleancompose.data.repository.RepoRepositoryImpl
import com.github.cleancompose.domain.repository.ConnectivityRepository
import com.github.cleancompose.domain.repository.RepoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindRepoRepository(repository: RepoRepositoryImpl): RepoRepository

    @Binds
    abstract fun bindConnectivityRepository(repository: ConnectivityRepositoryImpl): ConnectivityRepository
}
