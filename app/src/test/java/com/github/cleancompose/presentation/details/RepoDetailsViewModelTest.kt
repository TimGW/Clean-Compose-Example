package com.github.cleancompose.presentation.details

import androidx.lifecycle.SavedStateHandle
import com.github.cleancompose.R
import com.github.cleancompose.domain.model.repo.Repo
import com.github.cleancompose.domain.model.state.Result.ErrorType
import com.github.cleancompose.domain.usecase.repo.GetNetworkStatusUseCase
import com.github.cleancompose.domain.usecase.repo.GetRepoDetailsUseCase
import com.github.cleancompose.presentation.repo.screens.details.RepoDetailsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class RepoDetailsViewModelTest {
    @Mock
    private lateinit var getRepoDetailsUseCase: GetRepoDetailsUseCase

    @Mock
    private lateinit var getNetworkStatusUseCase: GetNetworkStatusUseCase
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
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = RepoDetailsViewModel(
            getRepoDetailsUseCase,
            getNetworkStatusUseCase,
            SavedStateHandle().apply {
                val json = Json.encodeToString(testRepo)
                set("repo", json)
                set("pageTitle", "title")
            },
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
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