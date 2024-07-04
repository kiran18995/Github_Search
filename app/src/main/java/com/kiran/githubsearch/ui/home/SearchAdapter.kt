package com.kiran.githubsearch.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.kiran.githubsearch.R
import com.kiran.githubsearch.data.models.Repo
import com.kiran.githubsearch.databinding.ItemSearchRepositoryBinding

class SearchAdapter : PagingDataAdapter<Repo, SearchAdapter.SearchViewHolder>(DiffCallback()) {
    class SearchViewHolder(private val binding: ItemSearchRepositoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(repo: Repo) {
            binding.repositoryLogo.load(repo.owner.avatarUrl){
                placeholder(showPlaceHolderShimmer())
            }
            val ownerName = "@" + repo.owner.login
            binding.repoName.text = repo.name
            binding.ownerName.text = ownerName
            binding.repoDesc.text = repo.description
            binding.starCount.text = repo.stars.toString()
            binding.forkCount.text = repo.forks.toString()
        }

        private fun showPlaceHolderShimmer(): ShimmerDrawable {
            val shimmer = Shimmer.ColorHighlightBuilder()
                .setBaseColor(ContextCompat.getColor(itemView.context, R.color.colorPrimary))
                .setBaseAlpha(0.7f).setHighlightAlpha(0.7f)
                .setHighlightColor(ContextCompat.getColor(itemView.context, R.color.colorPrimaryLight))
                .setDuration(600).setDirection(Shimmer.Direction.LEFT_TO_RIGHT).setAutoStart(true)
                .build()
            return ShimmerDrawable().apply {
                setShimmer(shimmer)
            }
        }
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val repo = getItem(position)
        if (repo != null) {
            holder.bind(repo)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding =
            ItemSearchRepositoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }
}

class DiffCallback : DiffUtil.ItemCallback<Repo>() {
    override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean {
        return oldItem == newItem
    }
}