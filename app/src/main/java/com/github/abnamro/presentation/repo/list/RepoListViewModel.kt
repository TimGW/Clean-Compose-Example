package com.github.abnamro.presentation.repo.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.github.abnamro.domain.model.state.ConnectionState
import com.github.abnamro.domain.usecase.repo.GetNetworkStatusUseCase
import com.github.abnamro.domain.usecase.repo.GetReposUseCase
import com.github.abnamro.domain.usecase.repo.GetReposUseCaseImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class RepoListViewModel @Inject constructor(
    getReposUseCase: GetReposUseCase,
    getNetworkStatusUseCase: GetNetworkStatusUseCase,
) : ViewModel() {

    val networkStatus: Flow<Boolean> = getNetworkStatusUseCase.execute(Unit)

    // TODO also create separate UiState model
    val uiState = getReposUseCase.execute(GetReposUseCaseImpl.Params()).cachedIn(viewModelScope)
}