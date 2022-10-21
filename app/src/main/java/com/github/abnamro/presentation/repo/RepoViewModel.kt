package com.github.abnamro.presentation.repo

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class RepoViewModel @Inject constructor() : ViewModel() {

    private val _isNetworkAvailable = MutableStateFlow<Boolean?>(null)
    val isNetworkAvailable: StateFlow<Boolean?> = _isNetworkAvailable

    fun updateNetworkAvailability(isAvailable: Boolean) {
        _isNetworkAvailable.value = isAvailable
    }
}