package com.github.abnamro.presentation.repo.details

import android.net.Uri
import com.github.abnamro.domain.model.repo.RepoDetails

data class RepoDetailsUiState(
    val dataState: RepoDetails? = null,
    val loadingState: Boolean? = null,
    val errorState: Boolean? = null,
    val message: Int? = null,
    val uri: Uri? = null
)
