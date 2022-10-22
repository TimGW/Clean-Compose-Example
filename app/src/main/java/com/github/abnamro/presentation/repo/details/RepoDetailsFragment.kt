package com.github.abnamro.presentation.repo.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.github.abnamro.R
import com.github.abnamro.databinding.FragmentRepoDetailsBinding
import com.github.abnamro.domain.model.repo.RepoDetails
import com.github.abnamro.presentation.base.snackbar
import com.github.abnamro.presentation.repo.RepoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class RepoDetailsFragment : Fragment() {
    private val sharedViewModel by activityViewModels<RepoViewModel>()
    private val viewModel by viewModels<RepoDetailsViewModel>()
    private var _binding: FragmentRepoDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRepoDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.swiperefresh.setOnRefreshListener {
            viewModel.fetchRepoDetails(forceRefresh = true)
        }
        observeUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeUI() {
        viewLifecycleOwner.lifecycleScope.launch {
            sharedViewModel.isNetworkAvailable.collectLatest { isAvailable ->
                isAvailable?.let { if (it) viewModel.fetchRepoDetails() }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    uiState.errorState?.let { showErrorState() }
                    uiState.loadingState?.let { showLoadingState(it) }
                    uiState.dataState?.let { showDataState(it) }
                    uiState.message?.let { showMessage(it) }
                    uiState.uri?.let { openUri(it) }
                }
            }
        }
    }

    private fun showErrorState() {
        binding.noDataState.visibility = View.VISIBLE
        binding.noDataState.text = getString(R.string.fragment_repo_details_error)
        binding.repoDetailsCta.visibility = View.GONE
    }

    private fun showLoadingState(isLoading: Boolean) {
        binding.swiperefresh.isRefreshing = isLoading
    }

    private fun showDataState(repoDetails: RepoDetails) {
        binding.noDataState.visibility = View.GONE
        binding.repoDetailsCta.visibility = View.VISIBLE

        binding.repoDetailsCta.setOnClickListener {
            viewModel.onRepoDetailsCtaClick(repoDetails.htmlURL)
        }

        binding.repoDetailsImage.apply {
            Glide.with(context)
                .load(repoDetails.owner.avatarURL)
                .placeholder(R.drawable.ic_placeholder)
                .centerCrop()
                .dontAnimate()
                .into(this)
        }

        binding.repoDetailsName.text = context?.getString(
            R.string.fragment_repo_details_name,
            repoDetails.name
        )
        binding.repoDetailsFullName.text = context?.getString(
            R.string.fragment_repo_details_full_name,
            repoDetails.fullName
        )
        binding.repoDetailsDescription.text = context?.getString(
            R.string.fragment_repo_details_description,
            repoDetails.description
        )
        binding.repoDetailsVisibility.text = context?.getString(
            R.string.fragment_repo_details_visibility,
            repoDetails.visibility
        )
        binding.repoDetailsPrivate.text = context?.getString(
            R.string.fragment_repo_details_private,
            repoDetails.isPrivate.toString()
        )
        binding.repoDetailsLastUpdate.text = context?.getString(
            R.string.fragment_repo_details_last_update,
            SimpleDateFormat("HH:mm", Locale.getDefault()) // TODO: do formatting in VM
                .format(Date(repoDetails.modifiedAt))
        )
    }

    private fun showMessage(message: Int) {
        view?.snackbar(getString(message))
        viewModel.onMessageShown()
    }

    private fun openUri(uri: Uri) {
        context?.startActivity(Intent(Intent.ACTION_VIEW, uri))
        viewModel.onUriOpened()
    }
}
