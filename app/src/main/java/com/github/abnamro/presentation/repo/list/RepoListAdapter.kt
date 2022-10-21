package com.github.abnamro.presentation.repo.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.github.abnamro.R
import com.github.abnamro.databinding.ListItemRepoBinding
import com.github.abnamro.domain.model.repo.Repo

private val DIFF_UTIL_REPO = object : DiffUtil.ItemCallback<Repo>() {
    override fun areItemsTheSame(oldItem: Repo, newItem: Repo) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Repo, newItem: Repo) = oldItem == newItem
}

class RepoListAdapter : PagingDataAdapter<Repo, RepoListAdapter.ViewHolder>(DIFF_UTIL_REPO) {
    var clickListener: ((Repo) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ViewHolder(
        ListItemRepoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun getItemViewType(position: Int): Int = R.layout.list_item_repo

    inner class ViewHolder(
        private val binding: ListItemRepoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Repo) {
            val ctx = binding.root.context
            binding.repoItemName.text = item.name
            binding.repoItemVisibility.text =
                ctx.getString(R.string.fragment_repo_list_item_visibility, item.visibility)
            binding.repoItemPrivate.text =
                ctx.getString(R.string.fragment_repo_list_item_private, item.isPrivate.toString())

            binding.repoItemImage.apply {
                Glide.with(context)
                    .load(item.owner.avatarURL)
                    .placeholder(R.drawable.ic_placeholder)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(this)
            }

            binding.root.setOnClickListener {
                clickListener?.invoke(item)
            }
        }
    }
}
