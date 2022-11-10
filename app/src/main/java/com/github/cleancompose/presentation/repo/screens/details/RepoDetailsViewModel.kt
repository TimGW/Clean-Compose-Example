package com.github.cleancompose.presentation.repo.screens.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.cleancompose.R
import com.github.cleancompose.data.di.IoDispatcher
import com.github.cleancompose.domain.model.repo.Repo
import com.github.cleancompose.domain.model.repo.RepoDetails
import com.github.cleancompose.domain.model.state.Result
import com.github.cleancompose.domain.usecase.invoke
import com.github.cleancompose.domain.usecase.repo.GetNetworkStatusUseCase
import com.github.cleancompose.domain.usecase.repo.GetRepoDetailsUseCase
import com.github.cleancompose.domain.usecase.repo.GetRepoDetailsUseCaseImpl
import com.github.cleancompose.presentation.repo.navigation.RepoDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class RepoDetailsViewModel @Inject constructor(
    private val getRepoDetailsUseCase: GetRepoDetailsUseCase,
    getNetworkStatusUseCase: GetNetworkStatusUseCase,
    savedStateHandle: SavedStateHandle,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val initialRepo: RepoDetails =
        checkNotNull(savedStateHandle[RepoDestination.DetailsScreen.repoArg]).let {
            Json.decodeFromString<Repo>(it as String).toDetails()
        }
    private val _uiState: MutableStateFlow<RepoDetailsUiState> =
        MutableStateFlow(RepoDetailsUiState.Initial(initialRepo))
    val uiState: StateFlow<RepoDetailsUiState> = _uiState.asStateFlow()
    val networkStatus: Flow<Boolean> = getNetworkStatusUseCase.invoke()

    fun fetchRepoDetails(
        forceRefresh: Boolean = false,
        query: String = initialRepo.fullName
    ) = viewModelScope.launch(dispatcher) {
        getRepoDetailsUseCase.invoke(
            GetRepoDetailsUseCaseImpl.Params(query, forceRefresh)
        ).collect { value ->
            _uiState.update { updateUiState(value) }
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
    ): RepoDetailsUiState = when (result) {
        is Result.Error -> {
            val msg = getMessageForError(result.error)
            if (result.data == null) {
                RepoDetailsUiState.FatalError(msg)
            } else {
                RepoDetailsUiState.SoftError(result.data, msg)
            }
        }
        is Result.Loading -> {
            RepoDetailsUiState.Loading(result.data)
        }
        is Result.Success -> {
            result.data?.let { RepoDetailsUiState.Success(it) } ?: RepoDetailsUiState.Empty
        }
    }
}