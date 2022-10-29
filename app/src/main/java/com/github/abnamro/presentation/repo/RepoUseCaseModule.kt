package com.github.abnamro.presentation.repo

import com.github.abnamro.domain.usecase.repo.GetNetworkStatusUseCase
import com.github.abnamro.domain.usecase.repo.GetNetworkStatusUseCaseImpl
import com.github.abnamro.domain.usecase.repo.GetRepoDetailsUseCase
import com.github.abnamro.domain.usecase.repo.GetRepoDetailsUseCaseImpl
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
 *
 * @ViewModelScoped Binds a single instance of the return type and is provided across all dependencies
 * injected into the ViewModel. Other instances of ViewModel will receive a different instance.
 */
@Module
@InstallIn(ViewModelComponent::class)
abstract class RepoUseCaseModule {

    /**
     * Get a paged result of repository's for a user
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

    /**
     * Get the details of a repository
     *
     * @param getRepoDetailsUseCaseImpl: the implementation of the UseCase
     *
     * @return UseCase to get detailed repo
     */
    @Binds
    @ViewModelScoped
    abstract fun provideGetRepoDetailsUseCase(
        getRepoDetailsUseCaseImpl: GetRepoDetailsUseCaseImpl
    ): GetRepoDetailsUseCase

    /**
     * Listen to changes in network status
     *
     * @param getNetworkStatusUseCaseImpl: the implementation of the UseCase
     *
     * @return UseCase for network listener
     */
    @Binds
    @ViewModelScoped
    abstract fun provideGetNetworkStatusUseCase(
        getNetworkStatusUseCaseImpl: GetNetworkStatusUseCaseImpl
    ): GetNetworkStatusUseCase
}