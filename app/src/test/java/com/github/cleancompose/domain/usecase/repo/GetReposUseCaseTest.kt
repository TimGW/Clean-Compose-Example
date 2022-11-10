package com.github.cleancompose.domain.usecase.repo

import com.github.cleancompose.domain.repository.RepoRepository
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class GetReposUseCaseTest {
    private lateinit var useCase: GetReposUseCaseImpl
    private val repository: RepoRepository = mockk(relaxed = true)

    @Before
    fun beforeTest() {
        useCase = GetReposUseCaseImpl(repository)
    }

    @Test
    fun invalidParams() {
        useCase.invoke(GetReposUseCaseImpl.Params("Tim"))

        verify { repository.getPagedRepos("Tim") }
    }
}