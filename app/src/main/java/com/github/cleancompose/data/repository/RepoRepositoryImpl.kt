package com.github.cleancompose.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.map
import com.github.cleancompose.data.di.IoDispatcher
import com.github.cleancompose.data.local.AppDatabase
import com.github.cleancompose.data.local.PAGE_SIZE
import com.github.cleancompose.data.local.RepoRemoteMediator
import com.github.cleancompose.data.model.RepoDetailsEntity
import com.github.cleancompose.data.model.RepoEntity
import com.github.cleancompose.data.remote.RepoService
import com.github.cleancompose.domain.model.repo.Repo
import com.github.cleancompose.domain.model.repo.RepoDetails
import com.github.cleancompose.domain.model.state.ErrorHandler
import com.github.cleancompose.domain.model.state.Result
import com.github.cleancompose.domain.repository.RepoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RepoRepositoryImpl @Inject constructor(
    private val repoService: RepoService,
    private val database: AppDatabase,
    private val errorHandler: ErrorHandler,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : RepoRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getPagedRepos(userName: String): Flow<PagingData<Repo>> {
        val pagingSourceFactory: () -> PagingSource<Int, RepoEntity> =
            { database.repoDao().getAllRepos() }
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            remoteMediator = RepoRemoteMediator(
                userName = userName,
                repoService = repoService,
                database = database
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow.map { pagingData ->
            pagingData.map { it.toRepo() }
        }
    }

    override fun getRepoDetails(
        query: String,
        forceRefresh: Boolean?,
    ): Flow<Result<RepoDetails?>> =
        object : NetworkBoundResource<RepoDetailsEntity, RepoDetails?>(errorHandler) {

            override suspend fun saveRemoteData(response: RepoDetailsEntity) {
                database.repoDetailsDao().insertRepoDetails(response)
            }

            override fun fetchFromLocal() =
                database.repoDetailsDao().getRepoDetailsDistinct(query).map {
                    it?.toDetails()
                }

            override suspend fun fetchFromRemote() = repoService.getRepoDetails(query)

            override fun shouldFetch(data: RepoDetails?) =
                (data == null || forceRefresh == true || isStale(data.modifiedAt))

        }.asFlow().flowOn(dispatcher)

    private fun isStale(lastUpdated: Long): Boolean {
        val oneDay = TimeUnit.DAYS.toMillis(1)
        return (System.currentTimeMillis() - oneDay) > lastUpdated
    }
}