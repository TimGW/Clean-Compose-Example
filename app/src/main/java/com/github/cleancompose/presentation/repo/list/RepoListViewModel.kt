package com.github.cleancompose.presentation.repo.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.github.cleancompose.domain.usecase.repo.GetNetworkStatusUseCase
import com.github.cleancompose.domain.usecase.repo.GetReposUseCase
import com.github.cleancompose.domain.usecase.repo.GetReposUseCaseImpl
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