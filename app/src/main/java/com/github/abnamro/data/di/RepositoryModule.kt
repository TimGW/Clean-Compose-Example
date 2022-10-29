package com.github.abnamro.data.di

import com.github.abnamro.data.repository.ConnectivityRepositoryImpl
import com.github.abnamro.data.repository.RepoRepositoryImpl
import com.github.abnamro.domain.repository.ConnectivityRepository
import com.github.abnamro.domain.repository.RepoRepository
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
