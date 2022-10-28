package com.github.abnamro.presentation.repo.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.github.abnamro.domain.model.repo.Repo
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.flow.Flow

@Composable
fun RepoList(
    viewModel: RepoListViewModel = hiltViewModel(),
    onRepoClick: (String) -> Unit = {},
) {
    val items = viewModel.uiState.collectAsLazyPagingItems()
    val state = rememberSwipeRefreshState(
        isRefreshing = items.loadState.refresh is LoadState.Loading,
    )
    SwipeRefresh(
        state = state,
        onRefresh = { items.refresh() },
    ) {
        RepoListContent(list = viewModel.uiState)
    }
}

@Composable
fun RepoListContent(
    list: Flow<PagingData<Repo>>,
    onRepoClick: (String) -> Unit = {}
) {
    val pagingItems: LazyPagingItems<Repo> = list.collectAsLazyPagingItems()
    val ctx = LocalContext.current
    val navController = rememberNavController()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
    ) {
        items(pagingItems) { item ->
            item?.let { RepoListItem(repo = it, onRepoClick = onRepoClick) }
        }
        pagingItems.apply {
            when {
                loadState.refresh is LoadState.NotLoading -> {
                    // TODO
                    item {
                        if (pagingItems.itemCount == 0) {
                            Text(
                                modifier = Modifier.padding(16.dp),
                                text = "error",
                            )
                        }
                    }
                }
                loadState.refresh is LoadState.Loading -> {
                    //You can add modifier to manage load state when first time response page is loading
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
                    //You can use modifier to show error message
                    item {
                        // todo
                        Text(
                            modifier = Modifier.padding(16.dp),
                            text = "error",
                        )
                    }
                }
            }
        }
    }
}
