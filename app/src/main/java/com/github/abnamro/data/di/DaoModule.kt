package com.github.abnamro.data.di

import com.github.abnamro.data.local.AppDatabase
import com.github.abnamro.data.local.RepoDetailsDao
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