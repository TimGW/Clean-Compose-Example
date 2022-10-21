package com.github.abnamro.data.local

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.github.abnamro.data.remote.RepoService
import com.github.abnamro.domain.model.repo.Repo
import retrofit2.HttpException
import java.io.IOException

const val PAGE_SIZE = 10
const val START_INDEX = 1

class RepoPagingSource (
    private val userName: String,
    private val service: RepoService,
) : PagingSource<Int, Repo>() {

    //todo https://developer.android.com/topic/libraries/architecture/paging/test
    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, Repo> {
        return try {
            val nextPageNumber = params.key ?: START_INDEX
            val response =  service.getRepos(
                userName = userName,
                page = nextPageNumber,
                perPage = PAGE_SIZE
            )
            val repos = response.body() ?: run { return LoadResult.Error(Throwable()) }

            // get next link from headers
            val nextKey = response.headers()["Link"]?.let { link ->
                val regex = Regex("rel=\"next\"")
                if (regex.containsMatchIn(link)) nextPageNumber + 1 else null
            }

            LoadResult.Page(
                data = repos,
                prevKey = null, // only paging forward
                nextKey = nextKey
            )
        } catch (e: IOException) {
            // IOException for network failures.
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            // HttpException for any non-2xx HTTP status codes.
            return LoadResult.Error(e)
        }
    }

    // anchorPage is the initial page, so just return null.
    override fun getRefreshKey(state: PagingState<Int, Repo>): Int? = null
}