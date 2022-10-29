@file:Suppress("unused")

package com.github.cleancompose.domain.model.state

/** Wrapper class to handle typical network interactions for data, errors and loading state */
sealed class Result<out T>(
    /** optional available data */
    open val data: T? = null,
) {
    /** Success state */
    class Success<T>(override val data: T) : Result<T>(data)
    /** Loading state */
    class Loading<T>(data: T? = null) : Result<T>(data)
    /** Error state */
    class Error<T>(val error: ErrorType, data: T? = null) : Result<T>(data)

    /** Possible error types when an operation failed */
    sealed class ErrorType(
        /** optional available cause */
        val throwable: Throwable? = null,
        /** optional available message */
        val message: Int? = null
    ) {
        /** Database error */
        class DatabaseError(throwable: Throwable? = null) : ErrorType(throwable)
        /** Network error */
        class IOError(throwable: Throwable? = null) : ErrorType(throwable)
        /** Server error */
        class HttpError(throwable: Throwable? = null, val statusCode: Int) : ErrorType(throwable)
        /** Other errors */
        class Unknown(throwable: Throwable? = null) : ErrorType(throwable)
    }
}