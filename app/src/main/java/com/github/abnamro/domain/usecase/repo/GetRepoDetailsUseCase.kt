package com.github.abnamro.domain.usecase.repo

import com.github.abnamro.domain.model.repo.RepoDetails
import com.github.abnamro.domain.model.state.Result
import com.github.abnamro.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow

/** Marker interface for GetRepoDetailsUseCase, preventing the need of @JvmSuppressWildcards */
interface GetRepoDetailsUseCase : UseCase<GetRepoDetailsUseCaseImpl.Params, Flow<Result<RepoDetails?>>>