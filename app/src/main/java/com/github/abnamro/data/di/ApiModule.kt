package com.github.abnamro.data.di

import com.github.abnamro.data.remote.RepoService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    fun provideRepoService(
        retrofit: Retrofit
    ): RepoService = retrofit.create(RepoService::class.java)
}