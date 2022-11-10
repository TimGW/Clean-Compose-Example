package com.github.cleancompose.domain.usecase.repo

import com.github.cleancompose.domain.repository.ConnectivityRepository
import com.github.cleancompose.domain.usecase.invoke
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class GetNetworkStatusUseCaseImplTest {
    private lateinit var useCase: GetNetworkStatusUseCaseImpl
    private val repository: ConnectivityRepository = mockk(relaxed = true)

    @Before
    fun beforeTest() {
        useCase = GetNetworkStatusUseCaseImpl(repository)
    }

    @Test
    fun invalidParams() {
        useCase.invoke()

        verify { repository.connectionState }
    }
}