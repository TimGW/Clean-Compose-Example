package com.github.cleancompose.domain.model.state

/** Interface to handle Network and other Exceptions */
interface ErrorHandler {

    /**
     * Determines which other ErrorType to return based on the Throwable.
     *
     * @param throwable: The error that is thrown
     *
     * @return a `Result.ErrorType`
     */
    fun getError(throwable: Throwable): Result.ErrorType

    /**
     * Determines which Network ErrorType to return based on the statusCode and Throwable.
     *
     * @param statusCode: The Http code
     * @param throwable: The error that is thrown
     *
     * @return a `Result.ErrorType`
     */
    fun getApiError(statusCode: Int, throwable: Throwable? = null): Result.ErrorType
}