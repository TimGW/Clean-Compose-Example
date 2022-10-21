package com.github.abnamro.presentation.repo

import com.github.abnamro.domain.usecase.repo.GetReposUseCase
import com.github.abnamro.domain.usecase.repo.GetReposUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

/**
 * Module to inject a UseCase into a ViewModel.
 * ViewModelComponent follows the lifecycle of a ViewModel
 */
@Module
@InstallIn(ViewModelComponent::class)
abstract class RepoUseCaseModule {

    /**
     * Binds single instance of the return type and is provided across all dependencies injected
     * into the ViewModel. Other instances of ViewModel will receive a different instance.
     *
     * @param getReposUseCaseImpl: the implementation of the UseCase
     *
     * @return UseCase to retrieve paged repo's
     */
    @Binds
    @ViewModelScoped
    abstract fun provideGetReposPagedUseCase(
        getReposUseCaseImpl: GetReposUseCaseImpl
    ): GetReposUseCase
}