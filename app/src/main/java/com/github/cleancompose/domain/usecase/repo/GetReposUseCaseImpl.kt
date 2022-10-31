package com.github.cleancompose.domain.usecase.repo

import androidx.paging.PagingData
import com.github.cleancompose.domain.model.repo.Repo
import com.github.cleancompose.domain.repository.RepoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetReposUseCaseImpl @Inject constructor(
    private val repository: RepoRepository,
) : GetReposUseCase {

    data class Params(val user: String)

    override fun invoke(
        params: Params
    ): Flow<PagingData<Repo>> = repository.getPagedRepos(params.user)
}

const val DEFAULT_USER = "timgw"
