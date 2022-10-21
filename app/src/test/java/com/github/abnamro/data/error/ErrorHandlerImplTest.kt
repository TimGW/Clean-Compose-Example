package com.github.abnamro.data.error

import com.github.abnamro.domain.model.state.ErrorHandler
import com.github.abnamro.domain.model.state.Result
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
class ErrorHandlerImplTest {

    private lateinit var errorHandler: ErrorHandler

    @Mock
    private lateinit var mockResponse: Response<*>

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