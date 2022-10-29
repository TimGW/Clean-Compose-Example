package com.github.cleancompose.domain.usecase.repo

import com.github.cleancompose.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow

interface GetNetworkStatusUseCase: UseCase<Unit, Flow<Boolean>>
