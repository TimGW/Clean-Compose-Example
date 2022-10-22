package com.github.abnamro.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.github.abnamro.data.local.PAGE_SIZE
import com.github.abnamro.data.local.RepoPagingSource
import com.github.abnamro.data.remote.RepoService
import com.github.abnamro.domain.model.repo.RepoDetails
import com.github.abnamro.domain.model.state.Result
import com.github.abnamro.domain.repository.RepoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepoRepositoryImpl @Inject constructor(
    private val repoService: RepoService,
) : RepoRepository {

    override fun getPagedRepos(userName: String) = Pager(
        PagingConfig(pageSize = PAGE_SIZE)
    ) {
        RepoPagingSource(userName, repoService)
    }.flow

    override fun getRepoDetails(query: String, forceRefresh: Boolean?): Flow<Result<RepoDetails?>> {
        TODO("Not yet implemented")
    }
}