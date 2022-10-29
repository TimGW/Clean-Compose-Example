package com.github.cleancompose.data.di

import com.github.cleancompose.data.local.AppDatabase
import com.github.cleancompose.data.local.RepoDetailsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    fun provideRepoDetailsDao(database: AppDatabase): RepoDetailsDao {
        return database.repoDetailsDao()
    }
}