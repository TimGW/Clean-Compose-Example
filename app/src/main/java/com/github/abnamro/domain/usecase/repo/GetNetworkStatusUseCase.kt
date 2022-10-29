package com.github.abnamro.domain.usecase.repo

import com.github.abnamro.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow

interface GetNetworkStatusUseCase: UseCase<Unit, Flow<Boolean>>
