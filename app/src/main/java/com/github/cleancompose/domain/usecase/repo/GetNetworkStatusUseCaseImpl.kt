package com.github.cleancompose.domain.usecase.repo

import com.github.cleancompose.domain.repository.ConnectivityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNetworkStatusUseCaseImpl @Inject constructor(
    private val repository: ConnectivityRepository,
) : GetNetworkStatusUseCase {

    override fun execute(
        params: Unit
    ): Flow<Boolean> = repository.connectionState
}
