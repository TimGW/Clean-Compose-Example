package com.github.cleancompose.domain.usecase.repo

import androidx.paging.PagingData
import com.github.cleancompose.domain.model.repo.Repo
import com.github.cleancompose.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow

/** Marker interface for GetReposUseCase, preventing the need of @JvmSuppressWildcards */
interface GetReposUseCase : UseCase<GetReposUseCaseImpl.Params, Flow<PagingData<Repo>>>