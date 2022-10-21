package com.github.abnamro.presentation.repo.list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.abnamro.R
import com.github.abnamro.databinding.ListItemRepoLoadStateBinding

class RepoListFooterLoadingState(
    private val retry: () -> Unit
) : LoadStateAdapter<RepoListFooterLoadingState.LoadStateViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ) = LoadStateViewHolder(parent, retry)

    override fun onBindViewHolder(
        holder: LoadStateViewHolder,
        loadState: LoadState
    ) = holder.bind(loadState)

    inner class LoadStateViewHolder(
        parent: ViewGroup,
        retry: () -> Unit
    ) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_repo_load_state, parent, false)
    ) {
        private val binding = ListItemRepoLoadStateBinding.bind(itemView)
        private val progressBar: ProgressBar = binding.loadStateItemProgress
        private val retry: Button = binding.loadStateItemRetry
            .also {
                it.setOnClickListener { retry() }
            }

        fun bind(loadState: LoadState) {
            progressBar.isVisible = loadState is LoadState.Loading
            retry.isVisible = loadState is LoadState.Error
        }
    }
}