package com.github.cleancompose.presentation.details

import androidx.lifecycle.SavedStateHandle
import com.github.cleancompose.R
import com.github.cleancompose.domain.model.state.Result.ErrorType
import com.github.cleancompose.domain.usecase.repo.GetRepoDetailsUseCase
import com.github.cleancompose.presentation.repo.screens.details.RepoDetailsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
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
    private lateinit var viewModel: RepoDetailsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = RepoDetailsViewModel(
            getRepoDetailsUseCase,
            SavedStateHandle().apply {
                set("query", "abnamrocoesd/connecting-components")
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

    @Test
    fun determineErrorMessage_null() {
        assertNull(viewModel.getMessageForError(null))
    }
}