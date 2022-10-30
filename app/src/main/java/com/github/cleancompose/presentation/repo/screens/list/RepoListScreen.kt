package com.github.cleancompose.presentation.repo.screens.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.github.cleancompose.R
import com.github.cleancompose.domain.model.repo.Repo
import com.github.cleancompose.presentation.core.ConnectivityStatus
import com.github.cleancompose.presentation.core.ShowTextScreen
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.flow.Flow

@Composable
fun RepoList(
    viewModel: RepoListViewModel = hiltViewModel(),
    onRepoClick: (Repo) -> Unit,
) {
    val items = viewModel.uiState.collectAsLazyPagingItems()
    val hasNetwork by viewModel.networkStatus.collectAsState(true)
    val state = rememberSwipeRefreshState(items.loadState.refresh is LoadState.Loading)

    LaunchedEffect(hasNetwork) {
        if (hasNetwork) items.refresh()
    }

    SwipeRefresh(
        state = state,
        swipeEnabled = hasNetwork,
        onRefresh = { items.refresh() },
    ) {
        Column {
            ConnectivityStatus(hasNetwork)
            RepoListContent(viewModel.uiState, onRepoClick)
        }
    }
}

@Composable
fun RepoListContent(
    list: Flow<PagingData<Repo>>,
    onRepoClick: (Repo) -> Unit
) {
    val pagingItems: LazyPagingItems<Repo> = list.collectAsLazyPagingItems()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
    ) {
        items(pagingItems) { item ->
            item?.let { RepoListItem(it, onRepoClick) }
        }
        pagingItems.apply {
            when {
                loadState.refresh is LoadState.NotLoading -> {
                    item {
                        if (pagingItems.itemCount == 0) {
                            ShowTextScreen(R.string.fragment_repo_list_empty)
                        }
                    }
                }
                loadState.refresh is LoadState.Error -> {
                    item { ShowTextScreen(R.string.fragment_repo_list_error) }
                }
                loadState.append is LoadState.Loading -> {
                    item {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .padding(16.dp)
                            )
                        }
                    }
                }
                loadState.append is LoadState.Error -> {
                    item { ShowTextScreen(R.string.fragment_repo_list_error) }
                }
            }
        }
    }
}
