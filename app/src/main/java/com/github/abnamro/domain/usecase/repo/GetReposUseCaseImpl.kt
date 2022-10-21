package com.github.abnamro.domain.usecase.repo

import androidx.paging.PagingData
import com.github.abnamro.domain.model.repo.Repo
import com.github.abnamro.domain.repository.RepoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetReposUseCaseImpl @Inject constructor(
    private val repository: RepoRepository,
) : GetReposUseCase {

    data class Params(val user: String = DEFAULT_USER)

    override fun execute(
        params: Params
    ): Flow<PagingData<Repo>> = repository.getPagedRepos(params.user)
}

private const val DEFAULT_USER = "abnamrocoesd"
