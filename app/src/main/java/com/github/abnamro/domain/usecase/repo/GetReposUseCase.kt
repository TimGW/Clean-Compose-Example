package com.github.abnamro.domain.usecase.repo

import androidx.paging.PagingData
import com.github.abnamro.domain.model.repo.Repo
import com.github.abnamro.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow

/** Marker interface for GetReposUseCase, preventing the need of @JvmSuppressWildcards */
interface GetReposUseCase : UseCase<GetReposUseCaseImpl.Params, Flow<PagingData<Repo>>>