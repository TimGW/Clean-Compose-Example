package com.github.cleancompose.domain.usecase.repo

import com.github.cleancompose.domain.repository.RepoRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetReposUseCaseTest {
    private lateinit var useCase: GetReposUseCaseImpl

    @Mock
    private lateinit var repository: RepoRepository

    @Before
    fun beforeTest() {
        useCase = GetReposUseCaseImpl(repository)
    }

    @Test
    fun invalidParams() {
        useCase.execute(GetReposUseCaseImpl.Params("Tim"))

        verify(repository).getPagedRepos("Tim")
    }
}