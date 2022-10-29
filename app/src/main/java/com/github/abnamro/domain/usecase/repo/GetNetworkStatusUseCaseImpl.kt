package com.github.abnamro.domain.usecase.repo

import com.github.abnamro.domain.repository.ConnectivityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNetworkStatusUseCaseImpl @Inject constructor(
    private val repository: ConnectivityRepository,
) : GetNetworkStatusUseCase {

    override fun execute(
        params: Unit
    ): Flow<Boolean> = repository.connectionState
}
