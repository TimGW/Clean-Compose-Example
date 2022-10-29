package com.github.cleancompose.domain.usecase

/** UseCase for business logic */
interface UseCase<in Params, out T> {
    /** execute the UseCase, provide `Unit` when you don't need params */
    fun execute(params: Params): T
}
