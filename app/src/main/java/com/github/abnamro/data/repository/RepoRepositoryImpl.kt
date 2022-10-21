package com.github.abnamro.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.github.abnamro.data.local.PAGE_SIZE
import com.github.abnamro.data.local.RepoPagingSource
import com.github.abnamro.data.remote.RepoService
import com.github.abnamro.domain.repository.RepoRepository
import javax.inject.Inject

class RepoRepositoryImpl @Inject constructor(
    private val repoService: RepoService,
) : RepoRepository {

    override fun getPagedRepos(userName: String) = Pager(
        PagingConfig(pageSize = PAGE_SIZE)
    ) {
        RepoPagingSource(userName, repoService)
    }.flow
}