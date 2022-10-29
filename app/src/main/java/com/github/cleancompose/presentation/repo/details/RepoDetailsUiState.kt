package com.github.cleancompose.presentation.repo.details

import com.github.cleancompose.domain.model.repo.RepoDetails

data class RepoDetailsUiState(
    val dataState: RepoDetails? = null,
    val loadingState: Boolean? = null,
    val errorState: Boolean? = null,
    val message: Int? = null,
)
