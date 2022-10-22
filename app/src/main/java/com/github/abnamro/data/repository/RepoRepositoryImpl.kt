package com.github.abnamro.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.github.abnamro.data.di.IoDispatcher
import com.github.abnamro.data.local.PAGE_SIZE
import com.github.abnamro.data.local.RepoDetailsDao
import com.github.abnamro.data.local.RepoPagingSource
import com.github.abnamro.data.model.RepoDetailsEntity
import com.github.abnamro.data.remote.RepoService
import com.github.abnamro.domain.model.repo.RepoDetails
import com.github.abnamro.domain.model.state.ErrorHandler
import com.github.abnamro.domain.repository.RepoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RepoRepositoryImpl @Inject constructor(
    private val repoService: RepoService,
    private val repoDetailsDao: RepoDetailsDao,
    private val errorHandler: ErrorHandler,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : RepoRepository {

    override fun getPagedRepos(userName: String) = Pager(
        PagingConfig(pageSize = PAGE_SIZE)
    ) {
        RepoPagingSource(userName, repoService)
    }.flow

    override fun getRepoDetails(
        query: String,
        forceRefresh: Boolean?,
    ) = object : NetworkBoundResource<RepoDetailsEntity, RepoDetails?>(errorHandler) {

        override suspend fun saveRemoteData(response: RepoDetailsEntity) {
            repoDetailsDao.insertRepoDetails(response)
        }

        override fun fetchFromLocal() = repoDetailsDao.getRepoDetailsDistinct(query).map {
            it?.toDetails()
        }

        override suspend fun fetchFromRemote() = repoService.getRepoDetails(query)

        override fun shouldFetch(
            data: RepoDetails?
        ) = (data == null || forceRefresh == true || isStale(data.modifiedAt))

    }.asFlow().flowOn(dispatcher)

    private fun isStale(lastUpdated: Long): Boolean {
        val oneDay = TimeUnit.DAYS.toMillis(1)
        return (System.currentTimeMillis() - oneDay) > lastUpdated
    }
}