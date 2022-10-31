package com.github.cleancompose.domain.usecase.repo

import com.github.cleancompose.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow

/** Marker interface preventing the need of @JvmSuppressWildcards and simplify the type */
interface GetNetworkStatusUseCase: UseCase<Unit, Flow<Boolean>>
