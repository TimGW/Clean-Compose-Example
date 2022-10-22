package com.github.abnamro.presentation.repo.details

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.abnamro.R
import com.github.abnamro.data.di.IoDispatcher
import com.github.abnamro.domain.model.repo.RepoDetails
import com.github.abnamro.domain.model.state.Result
import com.github.abnamro.domain.usecase.repo.GetRepoDetailsUseCase
import com.github.abnamro.domain.usecase.repo.GetRepoDetailsUseCaseImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepoDetailsViewModel @Inject constructor(
    private val getRepoDetailsUseCase: GetRepoDetailsUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val args = RepoDetailsFragmentArgs.fromSavedStateHandle(savedStateHandle)
    private val _uiState = MutableStateFlow(RepoDetailsUiState())
    val uiState: StateFlow<RepoDetailsUiState> = _uiState.asStateFlow()

    init {
        fetchRepoDetails()
    }

    fun fetchRepoDetails(forceRefresh: Boolean = false) {
        val result = getRepoDetailsUseCase.execute(GetRepoDetailsUseCaseImpl.Params(args.query, forceRefresh))
        viewModelScope.launch {
            result.onEach { updateUiState(it) }.collect()
        }
    }

    fun getMessageForError(error: Result.ErrorType?) = when (error) {
        is Result.ErrorType.DatabaseError -> R.string.error_database
        is Result.ErrorType.HttpError -> R.string.error_server
        is Result.ErrorType.IOError -> R.string.error_connection
        is Result.ErrorType.Unknown -> R.string.error_generic
        else -> null
    }

    fun onRepoDetailsCtaClick(htmlURL: String) {
        try {
            _uiState.update { it.copy(uri = Uri.parse(htmlURL)) }
        } catch (e: Exception) {
            _uiState.update { it.copy(message = R.string.fragment_repo_details_html_link_fail) }
        }
    }

    private fun updateUiState(
        result: Result<RepoDetails?>
    ) {
        when(result) {
            is Result.Error -> _uiState.update {
                it.copy(
                    dataState = result.data,
                    loadingState = false,
                    errorState = result.data == null,
                    message = getMessageForError(result.error)
                )
            }
            is Result.Loading -> _uiState.update {
                it.copy(
                    dataState = result.data,
                    loadingState = true,
                    errorState = false,
                    message = null
                )
            }
            is Result.Success -> _uiState.update {
                it.copy(
                    dataState = result.data,
                    loadingState = false,
                    errorState = result.data == null,
                    message = null
                )
            }
        }
    }

    // One off events need to reset the state to null. See:
    // https://developer.android.com/topic/architecture/ui-layer/events
    fun onUriOpened() { _uiState.update { it.copy(uri = null) } }
    fun onMessageShown() { _uiState.update { it.copy(message = null) } }
}