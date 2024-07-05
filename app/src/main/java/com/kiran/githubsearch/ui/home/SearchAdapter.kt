package com.kiran.githubsearch.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.kiran.githubsearch.R
import com.kiran.githubsearch.data.models.Repo
import com.kiran.githubsearch.databinding.ItemSearchRepositoryBinding

class SearchAdapter : PagingDataAdapter<Repo, SearchAdapter.SearchViewHolder>(DiffCallback()) {

    class SearchViewHolder(
        private val binding: ItemSearchRepositoryBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var currentRepo: Repo

        private companion object {
            private const val OWNER_NAME = "ownerName"
            private const val REPO_NAME = "repoName"
        }

        init {
            binding.container.setOnClickListener {
                val bundle = Bundle().apply {
                    putString(OWNER_NAME, currentRepo.owner.login)
                    putString(REPO_NAME, currentRepo.name)
                }
                Navigation.findNavController(it)
                    .navigate(R.id.actionSearchFragmentToRepositoryFragment, bundle)
            }
        }

        fun bind(repo: Repo) {
            currentRepo = repo
            binding.apply {
                repositoryLogo.load(repo.owner.avatarUrl) {
                    placeholder(R.drawable.github_logo)
                }
                val ownerNameValue = "@" + repo.owner.login
                repoName.text = repo.name
                ownerName.text = ownerNameValue
                repoDesc.text = repo.description ?: "No description available"
                starCount.text = repo.stars.toString()
                forkCount.text = repo.forks.toString()
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