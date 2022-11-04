package com.github.cleancompose.domain.usecase.repo

import com.github.cleancompose.domain.repository.RepoRepository
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class GetRepoDetailsUseCaseTest {
    private lateinit var useCase: GetRepoDetailsUseCase
    private val repository: RepoRepository = mockk(relaxed = true)

    @Before
    fun beforeTest() {
        useCase = GetRepoDetailsUseCaseImpl(repository)
    }

    @Test
    fun invalidParams() {
        val query = "abnamrocoesd/ansible-terraform"
        val refresh = false
        val params = GetRepoDetailsUseCaseImpl.Params(query, refresh)

        useCase(params)

        verify { repository.getRepoDetails(params.query, params.forceRefresh) }
    }
}