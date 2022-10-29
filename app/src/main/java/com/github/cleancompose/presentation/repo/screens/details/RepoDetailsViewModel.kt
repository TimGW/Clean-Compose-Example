package com.github.cleancompose.presentation.repo.screens.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.cleancompose.R
import com.github.cleancompose.domain.model.repo.RepoDetails
import com.github.cleancompose.domain.model.state.Result
import com.github.cleancompose.domain.usecase.repo.GetNetworkStatusUseCase
import com.github.cleancompose.domain.usecase.repo.GetRepoDetailsUseCase
import com.github.cleancompose.domain.usecase.repo.GetRepoDetailsUseCaseImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
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
    getNetworkStatusUseCase: GetNetworkStatusUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val query: String = checkNotNull(savedStateHandle["query"]) // todo: safe args?
    private val _uiState: MutableStateFlow<RepoDetailsUiState> =
        MutableStateFlow(RepoDetailsUiState.Initial)
    val uiState: StateFlow<RepoDetailsUiState> = _uiState.asStateFlow()
    val networkStatus: Flow<Boolean> = getNetworkStatusUseCase.execute(Unit)

    init {
        fetchRepoDetails()
    }

    fun fetchRepoDetails(forceRefresh: Boolean = false) {
        val result =
            getRepoDetailsUseCase.execute(GetRepoDetailsUseCaseImpl.Params(query, forceRefresh))
        viewModelScope.launch {
            result.onEach { updateUiState(it) }.collect()
        }
    }

    fun getMessageForError(error: Result.ErrorType) = when (error) {
        is Result.ErrorType.DatabaseError -> R.string.error_database
        is Result.ErrorType.HttpError -> R.string.error_server
        is Result.ErrorType.IOError -> R.string.error_connection
        is Result.ErrorType.Unknown -> R.string.error_generic
    }

    private fun updateUiState(
        result: Result<RepoDetails?>
    ) {
        when (result) {
            is Result.Error -> _uiState.update {
                val msg = getMessageForError(result.error)
                result.data?.let { RepoDetailsUiState.SoftError(it, msg) }
                    ?: RepoDetailsUiState.FatalError(msg)
            }
            is Result.Loading -> _uiState.update { RepoDetailsUiState.Loading(result.data) }
            is Result.Success -> _uiState.update {
                result.data?.let { RepoDetailsUiState.Success(it) } ?: RepoDetailsUiState.Empty
            }
        }
    }

//    fun onMessageShown() { _uiState.update { it.copy(message = null) } }
}