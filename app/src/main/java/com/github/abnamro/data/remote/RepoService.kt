package com.github.abnamro.data.remote

import com.github.abnamro.domain.model.repo.Repo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RepoService {

    @GET("users/{username}/repos")
    suspend fun getRepos(
        @Path("username") userName: String,
        @Query("type") type: String? = null,
        @Query("sort") sort: String? = null,
        @Query("direction") direction: String? = null,
        @Query("per_page") perPage: Int? = null,
        @Query("page") page: Int? = null,
    ): Response<List<Repo>>
}
