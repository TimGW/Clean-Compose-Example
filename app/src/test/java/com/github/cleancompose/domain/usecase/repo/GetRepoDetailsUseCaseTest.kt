package com.github.cleancompose.domain.usecase.repo

import com.github.cleancompose.domain.repository.RepoRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetRepoDetailsUseCaseTest {
    private lateinit var useCase: GetRepoDetailsUseCase

    @Mock
    private lateinit var repository: RepoRepository

    @Before
    fun beforeTest() {
        useCase = GetRepoDetailsUseCaseImpl(repository)
    }

    @Test
    fun invalidParams() {
        val query = "abnamrocoesd/ansible-terraform"
        val refresh = false
        val params = GetRepoDetailsUseCaseImpl.Params(query, refresh)

        useCase.execute(params)

        verify(repository).getRepoDetails(params.query, params.forceRefresh)
    }
}