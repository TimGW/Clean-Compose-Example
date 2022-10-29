package com.github.cleancompose.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.github.cleancompose.data.di.IoDispatcher
import com.github.cleancompose.data.local.PAGE_SIZE
import com.github.cleancompose.data.local.RepoDetailsDao
import com.github.cleancompose.data.local.RepoPagingSource
import com.github.cleancompose.data.model.RepoDetailsEntity
import com.github.cleancompose.data.remote.RepoService
import com.github.cleancompose.domain.model.repo.RepoDetails
import com.github.cleancompose.domain.model.state.ErrorHandler
import com.github.cleancompose.domain.repository.RepoRepository
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