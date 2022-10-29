package com.github.cleancompose.presentation.repo.screens.details

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.OpenInBrowser
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.github.cleancompose.R
import com.github.cleancompose.domain.model.repo.RepoDetails
import com.github.cleancompose.presentation.core.AnimatingFabContent
import com.github.cleancompose.presentation.core.ConnectivityStatus
import com.github.cleancompose.presentation.core.ShowTextScreen
import com.github.cleancompose.presentation.core.baselineHeight
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun RepoDetailsScreen(
    viewModel: RepoDetailsViewModel = hiltViewModel(),
) {
    val uiState: RepoDetailsUiState by viewModel.uiState.collectAsState()
    val loadingState = rememberSwipeRefreshState(uiState is RepoDetailsUiState.Loading)
    val scrollState = rememberScrollState()
    val hasNetwork by viewModel.networkStatus.collectAsState(true)
    val refresh = { viewModel.fetchRepoDetails(forceRefresh = true) }

    LaunchedEffect(hasNetwork) { if (hasNetwork) refresh() }

    SwipeRefresh(
        state = loadingState,
        swipeEnabled = hasNetwork,
        onRefresh = refresh
    ) {
        when (val state = uiState) {
            is RepoDetailsUiState.Initial -> { /* Do nothing */ }
            is RepoDetailsUiState.Loading -> {
                state.repoDetails?.let { RepoSuccessState(it, scrollState, hasNetwork) }
            }
            is RepoDetailsUiState.Success -> RepoSuccessState(
                state.repoDetails, scrollState, hasNetwork
            )
            RepoDetailsUiState.Empty -> ShowTextScreen(R.string.fragment_repo_details_empty)
            is RepoDetailsUiState.FatalError -> ShowTextScreen(state.message)
            is RepoDetailsUiState.SoftError -> RepoSoftErrorState(
                state.repoDetails,
                state.message,
                scrollState,
                hasNetwork
            )
        }
    }
}

@Composable
private fun RepoSuccessState(
    repoDetails: RepoDetails,
    scrollState: ScrollState,
    hasNetwork: Boolean,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        ConnectivityStatus(hasNetwork)
        BoxWithConstraints(modifier = Modifier.weight(1f)) {
            Surface {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState),
                ) {
                    RepoHeader(
                        scrollState,
                        repoDetails,
                        this@BoxWithConstraints.maxHeight
                    )
                    RepoContent(repoDetails, this@BoxWithConstraints.maxHeight)
                }
            }
            UriFab(
                cta = repoDetails.htmlURL,
                extended = scrollState.value == 0,
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
    }
}

@Composable
private fun RepoSoftErrorState(
    repoDetails: RepoDetails,
    message: Int,
    scrollState: ScrollState,
    hasNetwork: Boolean
) {
    RepoSuccessState(
        repoDetails = repoDetails,
        scrollState = scrollState,
        hasNetwork = hasNetwork,
    )
    showMessage(LocalContext.current, message = stringResource(message))
}

private fun showMessage(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

@Composable
private fun RepoHeader(
    scrollState: ScrollState,
    repo: RepoDetails,
    containerHeight: Dp
) {
    val offset = (scrollState.value / 2)
    val offsetDp = with(LocalDensity.current) { offset.toDp() }

    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(repo.owner.avatarURL)
            .crossfade(true)
            .build(),
        loading = {
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        },
        contentDescription = stringResource(R.string.image_description),
        modifier = Modifier
            .heightIn(max = containerHeight / 2)
            .fillMaxWidth()
            .padding(top = offsetDp),
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun RepoContent(repo: RepoDetails, containerHeight: Dp) {
    Column {
        Spacer(modifier = Modifier.height(8.dp))

        RepoName(repo)

        RepoProperty(stringResource(R.string.fragment_repo_details_full_name), repo.fullName)
        RepoProperty(
            stringResource(R.string.fragment_repo_details_description),
            repo.description
        )
        RepoProperty(stringResource(R.string.fragment_repo_details_visibility), repo.visibility)
        RepoProperty(
            stringResource(R.string.fragment_repo_details_private),
            repo.isPrivate.toString()
        )
        RepoProperty(
            stringResource(R.string.fragment_repo_details_last_update),
            SimpleDateFormat("HH:mm", Locale.getDefault()) // TODO: do formatting in VM
                .format(Date(repo.modifiedAt))
        )

        // Add a spacer that always shows part (320.dp) of the fields list regardless of the device,
        // in order to always leave some content at the top.
        Spacer(Modifier.height((containerHeight - 320.dp).coerceAtLeast(0.dp)))
    }
}

@Composable
private fun RepoName(
    repo: RepoDetails
) {
    Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
        Text(
            text = repo.name,
            modifier = Modifier.baselineHeight(32.dp),
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun RepoProperty(label: String, value: String, isLink: Boolean = false) {
    Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
        Divider()
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                text = label,
                modifier = Modifier.baselineHeight(24.dp),
                style = MaterialTheme.typography.caption,
            )
        }
        val style = if (isLink) {
            MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.primary)
        } else {
            MaterialTheme.typography.body1
        }
        Text(
            text = value,
            modifier = Modifier.baselineHeight(24.dp),
            style = style
        )
    }
}

@Composable
private fun UriFab(cta: String, extended: Boolean, modifier: Modifier = Modifier) {
    val uriHandler = LocalUriHandler.current

    FloatingActionButton(
        onClick = { uriHandler.openUri(cta) },
        modifier = modifier
            .padding(16.dp)
            .padding()
            .height(48.dp)
            .widthIn(min = 48.dp),
        backgroundColor = Color.Blue,
        contentColor = Color.White
    ) {
        AnimatingFabContent(
            icon = {
                Icon(
                    imageVector = Icons.Outlined.OpenInBrowser,
                    contentDescription = stringResource(R.string.fragment_repo_details_html_link)
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.fragment_repo_details_html_link),
                )
            },
            extended = extended

        )
    }
}