package com.github.cleancompose.domain.repository

import androidx.paging.PagingData
import com.github.cleancompose.domain.model.repo.Repo
import com.github.cleancompose.domain.model.repo.RepoDetails
import com.github.cleancompose.domain.model.state.Result
import kotlinx.coroutines.flow.Flow

/** Repository for interacting with GitHub repo's */
interface RepoRepository {

    /**
     * Get Paginated GitHub Repo's
     *
     * @param userName: The user to retrieve the repo's from
     *
     * @return Flow with paginated result
     */
    fun getPagedRepos(userName: String): Flow<PagingData<Repo>>

    /**
     * Get Single GitHub Repo
     *
     * @param query: The full `owner/repo` name
     *
     * @return Flow with detailed result
     */
    fun getRepoDetails(query: String, forceRefresh: Boolean?): Flow<Result<RepoDetails?>>
}
