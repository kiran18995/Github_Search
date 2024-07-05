package com.kiran.githubsearch.ui.repository

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.kiran.githubsearch.R
import com.kiran.githubsearch.data.models.Contributor
import com.kiran.githubsearch.databinding.ItemContributorBinding

class ContributorAdapter : RecyclerView.Adapter<ContributorAdapter.ContributorViewHolder>() {

    private var contributorList: List<Contributor> = emptyList()

    fun setContributors(contributors: List<Contributor>) {
        contributorList = contributors
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContributorViewHolder {
        val binding =
            ItemContributorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContributorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContributorViewHolder, position: Int) {
        holder.bind(contributorList[position])
    }

    override fun getItemCount(): Int {
        return contributorList.size
    }

    class ContributorViewHolder(private val binding: ItemContributorBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(contributor: Contributor) {
            binding.apply {
                contributorAvatar.load(contributor.avatarUrl) {
                    placeholder(R.drawable.github_logo)
                }
                contributorLogin.text = contributor.login
            }
        }
    }
}