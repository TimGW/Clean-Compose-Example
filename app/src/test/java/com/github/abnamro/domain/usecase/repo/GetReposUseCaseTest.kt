package com.github.abnamro.domain.usecase.repo

import com.github.abnamro.domain.repository.RepoRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetReposUseCaseTest {
    private lateinit var useCase: GetReposUseCase

    @Mock
    private lateinit var repository: RepoRepository

    @Before
    fun beforeTest() {
        useCase = GetReposUseCase(repository)
    }

    @Test
    fun invalidParams() {
        useCase.execute(GetReposUseCase.Params("Tim"))

        verify(repository).getPagedRepos("Tim")
    }
}