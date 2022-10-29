package com.github.cleancompose.domain.usecase.repo

import com.github.cleancompose.domain.model.repo.RepoDetails
import com.github.cleancompose.domain.model.state.Result
import com.github.cleancompose.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow

/** Marker interface for GetRepoDetailsUseCase, preventing the need of @JvmSuppressWildcards */
interface GetRepoDetailsUseCase : UseCase<GetRepoDetailsUseCaseImpl.Params, Flow<Result<RepoDetails?>>>