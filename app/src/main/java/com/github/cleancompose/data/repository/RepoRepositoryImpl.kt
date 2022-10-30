package com.github.cleancompose.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.github.cleancompose.data.di.IoDispatcher
import com.github.cleancompose.data.local.AppDatabase
import com.github.cleancompose.data.model.RepoDetailsEntity
import com.github.cleancompose.data.remote.RepoService
import com.github.cleancompose.domain.model.repo.Repo
import com.github.cleancompose.domain.model.repo.RepoDetails
import com.github.cleancompose.domain.model.state.ErrorHandler
import com.github.cleancompose.domain.model.state.Result
import com.github.cleancompose.domain.repository.RepoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
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
        val pagingSourceFactory = { database.repoDao().getAllRepos() }
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            remoteMediator = RepoRemoteMediator(
                userName = userName,
                repoService = repoService,
                database = database
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
            .flowOn(dispatcher)
            .map { pagingData -> pagingData.map { it.toDomain() } }
    }

    override fun getRepoDetails(
        query: String,
        forceRefresh: Boolean?
    ): Flow<Result<RepoDetails?>> = flow {
        emit(Result.Loading(null))

        val cachedData = database.repoDetailsDao().getRepoDetailsDistinct(query).firstOrNull()
        val fetchFromLocal = { database.repoDetailsDao().getRepoDetailsDistinct(query) }

        try {
            if (shouldFetch(cachedData, forceRefresh)) {
                emit(Result.Loading(cachedData?.toDetails()))

                val apiResponse = repoService.getRepoDetails(query)
                val remoteResponse = apiResponse.body()

                if (apiResponse.isSuccessful && remoteResponse != null) {
                    database.repoDetailsDao().insertRepoDetails(remoteResponse.toEntity())
                    emitAll(fetchFromLocal().map { Result.Success(it?.toDetails()) })
                } else {
                    emitAll(fetchFromLocal().map {
                        Result.Error(errorHandler.getApiError(apiResponse.code()), it?.toDetails())
                    })
                }
            } else {
                emit(Result.Success(cachedData?.toDetails()))
            }
        } catch (e: Exception) {
            emitAll(fetchFromLocal().map {
                Result.Error(
                    errorHandler.getError(e),
                    it?.toDetails()
                )
            })
        }
    }.flowOn(dispatcher)

    private fun shouldFetch(data: RepoDetailsEntity?, forceRefresh: Boolean?): Boolean =
        (data == null || forceRefresh == true || isStale(data.modifiedAt))

    private fun isStale(lastUpdated: Long): Boolean {
        val oneDay = TimeUnit.DAYS.toMillis(1)
        return (System.currentTimeMillis() - oneDay) > lastUpdated
    }
}