package com.github.cleancompose.presentation.repo.screens.details

import com.github.cleancompose.domain.model.repo.RepoDetails

sealed interface RepoDetailsUiState {
    class Initial(val repoDetails: RepoDetails) : RepoDetailsUiState
    object Empty : RepoDetailsUiState
    class Success(val repoDetails: RepoDetails) : RepoDetailsUiState
    class Loading(val repoDetails: RepoDetails?) : RepoDetailsUiState
    class FatalError(val message: Int) : RepoDetailsUiState
    class SoftError(val repoDetails: RepoDetails, val message: Int) : RepoDetailsUiState
}
