package com.github.cleancompose.domain.usecase

/**
 * UseCase for business logic.
 *
 * If generic types are exposed in Kotlin API, you need to consider @JvmSuppressWildcards
 * so that a Java consumer can compile successfully. Kotlin generics has declaration-site
 * variance and type projections. So to be compatible with DI tools like Dagger & Hilt,
 * Kotlin has @JvmSuppressWildcards and @JvmWildcard to suppress or force creating wildcards.
 */
interface UseCase<in Params: Any, out Result> {
    /** execute the UseCase, with params */
    operator fun invoke(params: Params): Result
}

/** execute UseCase, without params */
operator fun <Result> UseCase<Unit, Result>.invoke() = invoke(Unit)