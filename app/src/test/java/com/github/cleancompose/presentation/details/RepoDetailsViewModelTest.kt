package com.github.cleancompose.presentation.details

import androidx.lifecycle.SavedStateHandle
import com.github.cleancompose.R
import com.github.cleancompose.TestUtil
import com.github.cleancompose.domain.model.repo.Repo
import com.github.cleancompose.domain.model.state.Result
import com.github.cleancompose.domain.model.state.Result.ErrorType
import com.github.cleancompose.domain.usecase.repo.GetNetworkStatusUseCase
import com.github.cleancompose.domain.usecase.repo.GetRepoDetailsUseCase
import com.github.cleancompose.domain.usecase.repo.GetRepoDetailsUseCaseImpl
import com.github.cleancompose.presentation.repo.screens.details.RepoDetailsUiState
import com.github.cleancompose.presentation.repo.screens.details.RepoDetailsViewModel
import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RepoDetailsViewModelTest {
    private val getRepoDetailsUseCase: GetRepoDetailsUseCase = spyk()
    private val getNetworkStatusUseCase: GetNetworkStatusUseCase = spyk()
    private lateinit var viewModel: RepoDetailsViewModel
    private val testRepo = Repo(
        name = "",
        fullName = "",
        isPrivate = false,
        owner = Repo.Owner(login = "", avatarURL = ""),
        htmlURL = "",
        description = "",
        visibility = "",
        modifiedAt = 0L,
    )

    @Before
    fun setup() {
        viewModel = RepoDetailsViewModel(
            getRepoDetailsUseCase,
            getNetworkStatusUseCase,
            SavedStateHandle().apply {
                val json = Json.encodeToString(testRepo)
                set("repo", json)
                set("pageTitle", "title")
            },
            UnconfinedTestDispatcher()
        )
    }

    @Test
    fun fetchRepoDetails_initial() = runTest {
        val currentUiState: RepoDetailsUiState = viewModel.uiState.value
        val fullName = "TimGW/cleancomposeexample"
        val refresh = false
        val params = GetRepoDetailsUseCaseImpl.Params(fullName, refresh)

        viewModel.fetchRepoDetails(forceRefresh = refresh, query = fullName)

        verify { getRepoDetailsUseCase.invoke(params) }
        assertTrue(currentUiState is RepoDetailsUiState.Initial)
    }

    @Test
    fun fetchRepoDetails_updateUI_FatalError() = runTest {
        val fullName = "TimGW/cleancomposeexample"
        val refresh = false

        every { getRepoDetailsUseCase.invoke(any()) } returns flow {
            emit(Result.Error(error = ErrorType.DatabaseError(), data = null))
        }

        assertTrue(viewModel.uiState.value is RepoDetailsUiState.Initial)

        viewModel.fetchRepoDetails(forceRefresh = refresh, query = fullName)

        assertTrue(viewModel.uiState.value is RepoDetailsUiState.FatalError)
    }

    @Test
    fun fetchRepoDetails_updateUI_SoftError() = runTest {
        val fullName = "TimGW/cleancomposeexample"
        val refresh = false

        every { getRepoDetailsUseCase.invoke(any()) } returns flow {
            emit(Result.Error(error = ErrorType.DatabaseError(), data = TestUtil.createRepoDetails()))
        }

        assertTrue(viewModel.uiState.value is RepoDetailsUiState.Initial)

        viewModel.fetchRepoDetails(forceRefresh = refresh, query = fullName)

        assertTrue(viewModel.uiState.value is RepoDetailsUiState.SoftError)
    }

    @Test
    fun fetchRepoDetails_updateUI_Loading() = runTest {
        val fullName = "TimGW/cleancomposeexample"
        val refresh = false
        every { getRepoDetailsUseCase.invoke(any()) } returns flow {
            emit(Result.Loading())
        }

        assertTrue(viewModel.uiState.value is RepoDetailsUiState.Initial)

        viewModel.fetchRepoDetails(forceRefresh = refresh, query = fullName)

        assertTrue(viewModel.uiState.value is RepoDetailsUiState.Loading)
    }

    @Test
    fun fetchRepoDetails_updateUI_Empty() = runTest {
        val fullName = "TimGW/cleancomposeexample"
        val refresh = false
        every { getRepoDetailsUseCase.invoke(any()) } returns flow {
            emit(Result.Success(data = null))
        }

        assertTrue(viewModel.uiState.value is RepoDetailsUiState.Initial)

        viewModel.fetchRepoDetails(forceRefresh = refresh, query = fullName)

        assertTrue(viewModel.uiState.value is RepoDetailsUiState.Empty)
    }

    @Test
    fun fetchRepoDetails_updateUI_Success() = runTest {
        val fullName = "TimGW/cleancomposeexample"
        val refresh = false
        every { getRepoDetailsUseCase.invoke(any()) } returns flow {
            emit(Result.Success(data = TestUtil.createRepoDetails()))
        }

        assertTrue(viewModel.uiState.value is RepoDetailsUiState.Initial)

        viewModel.fetchRepoDetails(forceRefresh = refresh, query = fullName)

        assertTrue(viewModel.uiState.value is RepoDetailsUiState.Success)
    }

    @Test
    fun determineErrorMessage_database() {
        val input = ErrorType.DatabaseError()

        assertEquals(R.string.error_database, viewModel.getMessageForError(input))
    }

    @Test
    fun determineErrorMessage_http() {
        val input = ErrorType.HttpError(statusCode = 400)

        assertEquals(R.string.error_server, viewModel.getMessageForError(input))
    }

    @Test
    fun determineErrorMessage_io() {
        val input = ErrorType.IOError()

        assertEquals(R.string.error_connection, viewModel.getMessageForError(input))
    }

    @Test
    fun determineErrorMessage_unknown() {
        val input = ErrorType.Unknown()

        assertEquals(R.string.error_generic, viewModel.getMessageForError(input))
    }
}