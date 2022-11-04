package com.github.cleancompose.data.error

import com.github.cleancompose.domain.model.state.ErrorHandler
import com.github.cleancompose.domain.model.state.Result
import io.mockk.mockk
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class ErrorHandlerImplTest {
    private lateinit var errorHandler: ErrorHandler
    private val mockResponse: Response<*> = mockk(relaxed = true)

    @Before
    fun beforeTest() {
        errorHandler = ErrorHandlerImpl()
    }

    @Test
    fun testIOError() {
        val result = errorHandler.getError(IOException())
        assertTrue(result is Result.ErrorType.IOError)
    }

    @Test
    fun testHttpError() {
        val result = errorHandler.getError(HttpException(mockResponse))
        assertTrue(result is Result.ErrorType.HttpError)
    }

    @Test
    fun testOtherError() {
        val result = errorHandler.getError(Throwable())
        assertTrue(result is Result.ErrorType.Unknown)
    }

    @Test
    fun testApiError() {
        val result = errorHandler.getApiError(404, HttpException(mockResponse))
        assertTrue(result is Result.ErrorType.HttpError)
        assertTrue((result as Result.ErrorType.HttpError).statusCode == 404)
    }
}