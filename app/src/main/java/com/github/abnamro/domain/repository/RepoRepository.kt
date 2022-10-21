package com.github.abnamro.domain.repository

import androidx.paging.PagingData
import com.github.abnamro.domain.model.repo.Repo
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
}
