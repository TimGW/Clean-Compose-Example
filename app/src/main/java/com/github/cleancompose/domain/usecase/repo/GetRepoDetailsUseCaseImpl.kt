package com.github.cleancompose.domain.usecase.repo

import com.github.cleancompose.domain.model.repo.RepoDetails
import com.github.cleancompose.domain.model.state.Result
import com.github.cleancompose.domain.repository.RepoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import javax.inject.Inject

class GetRepoDetailsUseCaseImpl @Inject constructor(
    private val repository: RepoRepository,
) : GetRepoDetailsUseCase {

    data class Params(
        val query: String,
        val forceRefresh: Boolean
    )

    override fun invoke(
        params: Params
    ): Flow<Result<RepoDetails?>> {
        val result = repository.getRepoDetails(params.query, params.forceRefresh)
        val deboucedResult = result.debounce { if (it is Result.Loading) DEBOUNCE_TIMEOUT else 0L }
        return deboucedResult
    }
}

private const val DEBOUNCE_TIMEOUT = 400L
