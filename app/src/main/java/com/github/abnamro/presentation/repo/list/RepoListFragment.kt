package com.github.abnamro.presentation.repo.list

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.abnamro.R
import com.github.abnamro.databinding.FragmentRepoListBinding
import com.github.abnamro.domain.model.repo.Repo
import com.github.abnamro.presentation.base.snackbar
import com.github.abnamro.presentation.base.viewBinding
import com.github.abnamro.presentation.repo.RepoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RepoListFragment : Fragment(R.layout.fragment_repo_list) {
    private val sharedViewModel by activityViewModels<RepoViewModel>()
    private val viewModel by viewModels<RepoListViewModel>()
    private val repoListAdapter by lazy { RepoListAdapter() }
    private val binding by viewBinding(FragmentRepoListBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeUI()
        binding.swiperefresh.setOnRefreshListener { repoListAdapter.refresh() }
    }

    private fun setupRecyclerView() {
        val loadingAdapter = repoListAdapter
            .withLoadStateFooter(RepoListFooterLoadingState(repoListAdapter::retry))

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = loadingAdapter
            repoListAdapter.clickListener = { repo -> navigateToDetails(repo) }
            addItemDecoration(
                RepoListItemDecoration(
                    resources.getDimension(R.dimen.keyline_16).toInt()
                )
            )
        }
    }

    private fun observeUI() {
        viewLifecycleOwner.lifecycleScope.launch {
            sharedViewModel.isNetworkAvailable.collectLatest { isAvailable ->
                isAvailable?.let { if (it) repoListAdapter.retry() }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            // The submitData() method suspends and does not return until either the PagingSource
            // is invalidated or the adapter's refresh method is called.
            viewModel.uiState.collectLatest { repoListAdapter.submitData(it) }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repoListAdapter.loadStateFlow.collect { loadStates ->
                val loadState = loadStates.refresh
                binding.swiperefresh.isRefreshing = loadState is LoadState.Loading

                // TODO: improve sending Throwable to ViewModel to generate a better error message
                if (loadState is LoadState.Error) {
                    showError(loadState.error.localizedMessage)
                }
                if (loadState !is LoadState.Loading) {
                    binding.noDataState.isVisible = repoListAdapter.itemCount == 0
                }
            }
        }
    }

    private fun showError(message: String?) {
        view?.snackbar(message = message ?: getString(R.string.error_generic))
    }

    private fun navigateToDetails(repo: Repo) {
        val directions = RepoListFragmentDirections.showDetails(repo.name, repo.fullName)
        findNavController().navigate(directions)
    }
}
