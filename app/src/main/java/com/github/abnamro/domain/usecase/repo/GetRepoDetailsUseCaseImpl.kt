package com.github.abnamro.domain.usecase.repo

import com.github.abnamro.domain.model.repo.RepoDetails
import com.github.abnamro.domain.model.state.Result
import com.github.abnamro.domain.repository.RepoRepository
import com.github.abnamro.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetRepoDetailsUseCaseImpl @Inject constructor(
    private val repository: RepoRepository,
) : GetRepoDetailsUseCase {

    data class Params(
        val query: String,
        val forceRefresh: Boolean
    )

    override fun execute(
        params: Params
    ): Flow<Result<RepoDetails?>> {
        val result = repository.getRepoDetails(params.query, params.forceRefresh)
        return result
            .debounce { if (it is Result.Loading) DEBOUNCE_TIMEOUT else 0L }
            .map { it }
    }
}

private const val DEBOUNCE_TIMEOUT = 400L
