package com.github.abnamro.presentation.repo.details

import android.net.Uri
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.github.abnamro.R
import com.github.abnamro.domain.model.repo.RepoDetails
import com.github.abnamro.presentation.base.AnimatingFabContent
import com.github.abnamro.presentation.base.baselineHeight
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
    val repo = uiState.dataState ?: return
    val loadingState = rememberSwipeRefreshState(isRefreshing = uiState.loadingState ?: false)
    val scrollState = rememberScrollState()

    SwipeRefresh(
        state = loadingState,
        onRefresh = { viewModel.fetchRepoDetails(forceRefresh = true) },
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            BoxWithConstraints(modifier = Modifier.weight(1f)) {
                Surface {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState),
                    ) {
                        DetailsHeader(
                            scrollState,
                            repo,
                            this@BoxWithConstraints.maxHeight
                        )
                        RepoContent(repo, this@BoxWithConstraints.maxHeight)
                    }
                }
                UriFab(
                    cta = repo.htmlURL,
                    extended = scrollState.value == 0,
                    modifier = Modifier.align(Alignment.BottomEnd)
                )
            }
        }
    }
}

@Composable
private fun DetailsHeader(
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

        Name(repo)

        ProfileProperty(stringResource(R.string.fragment_repo_details_full_name), repo.fullName)
        ProfileProperty(
            stringResource(R.string.fragment_repo_details_description),
            repo.description
        )
        ProfileProperty(stringResource(R.string.fragment_repo_details_visibility), repo.visibility)
        ProfileProperty(
            stringResource(R.string.fragment_repo_details_private),
            repo.isPrivate.toString()
        )
        ProfileProperty(
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
private fun Name(
    repo: RepoDetails
) {
    Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
        Name(
            repo = repo,
            modifier = Modifier.baselineHeight(32.dp)
        )
    }
}

@Composable
private fun Name(repo: RepoDetails, modifier: Modifier = Modifier) {
    Text(
        text = repo.name,
        modifier = modifier,
        style = MaterialTheme.typography.h5,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun ProfileProperty(label: String, value: String, isLink: Boolean = false) {
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
fun UriFab(cta: String, extended: Boolean, modifier: Modifier = Modifier) {
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