package com.github.cleancompose.data.local

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.github.cleancompose.data.model.RepoEntity
import com.github.cleancompose.data.model.RepoKeysEntity
import com.github.cleancompose.data.remote.RepoService
import com.github.cleancompose.domain.model.repo.Repo
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.TimeUnit

const val PAGE_SIZE = 10

/**
 * RemoteMediator for a DB + Network based PagingData stream, which triggers network
 * requests to fetch additional items when a user scrolls to the end of the list of items stored
 * in DB.
 *
 * This sample loads a list of [Repo] via Retrofit from a page-keyed network service using
 * [String] tokens to load pages (each response has a next/previous token), and inserts them
 * into database.
 */
@OptIn(ExperimentalPagingApi::class)
class RepoRemoteMediator(
    private val userName: String,
    private val repoService: RepoService,
    private val database: AppDatabase
) : RemoteMediator<Int, RepoEntity>() {
    private val repoDao = database.repoDao()
    private val repoKeysDao = database.repoKeysDao()

    override suspend fun initialize() = if (shouldRefresh()) {
        InitializeAction.LAUNCH_INITIAL_REFRESH
    } else {
        InitializeAction.SKIP_INITIAL_REFRESH
    }

    //todo https://developer.android.com/topic/libraries/architecture/paging/test
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, RepoEntity>
    ): MediatorResult {
        return try {
            val currentPage: Int = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextPage?.minus(1) ?: 1
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prevPage
                        ?: return MediatorResult.Success(remoteKeys != null)
                    prevPage
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.nextPage
                        ?: return MediatorResult.Success(remoteKeys != null)
                    nextPage
                }
            }

            // fetch fresh repos from network
            val response: Response<List<RepoEntity>> = repoService.getRepos(
                userName = userName,
                page = currentPage,
                perPage = PAGE_SIZE
            )
            val reposEntity: List<RepoEntity> = response.body()
                ?: return MediatorResult.Error(Throwable("Response body is null"))

            val prevPage: Int? = response.headers()["Link"]?.let { link ->
                val regex = Regex("rel=\"prev\"")
                if (regex.containsMatchIn(link)) currentPage - 1 else null
            }
            val nextPage: Int? = response.headers()["Link"]?.let { link ->
                val regex = Regex("rel=\"next\"")
                if (regex.containsMatchIn(link)) currentPage + 1 else null
            }

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    repoDao.deleteAllRepos()
                    repoKeysDao.deleteAllRemoteKeys()
                }
                val keys = reposEntity.map { repo ->
                    RepoKeysEntity(
                        id = repo.fullName,
                        prevPage = prevPage,
                        nextPage = nextPage
                    )
                }
                repoKeysDao.addAllRemoteKeys(keys)
                repoDao.addRepos(reposEntity)
            }
            MediatorResult.Success(nextPage == null)
        } catch (e: IOException) {
            // IOException for network failures.
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            // HttpException for any non-2xx HTTP status codes.
            return MediatorResult.Error(e)
        }
    }

    private suspend fun shouldRefresh(): Boolean {
        val lastUpdated = repoDao.lastUpdated() ?: return true
        val oneDay = TimeUnit.HOURS.toMillis(1)
        return (System.currentTimeMillis() - oneDay) > lastUpdated
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, RepoEntity>
    ): RepoKeysEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.fullName?.let { repoKeysDao.getRemoteKeys(it) }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, RepoEntity>
    ): RepoKeysEntity? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { repoKeysDao.getRemoteKeys(it.fullName) }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, RepoEntity>
    ): RepoKeysEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { repoKeysDao.getRemoteKeys(it.fullName) }
    }
}
