package com.kiran.githubsearch.ui.repository

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.kiran.githubsearch.data.models.Repo
import com.kiran.githubsearch.databinding.FragmentRepositoryBinding
import com.kiran.githubsearch.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RepositoryFragment : Fragment() {

    private var _binding: FragmentRepositoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<RepositoryViewModel>()
    private var ownerName = ""
    private var repoName = ""
    private lateinit var contributorAdapter: ContributorAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRepositoryBinding.inflate(inflater, container, false)
        ownerName = arguments?.getString("ownerName") ?: ""
        repoName = arguments?.getString("repoName") ?: ""
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getQuotesGenres(ownerName, repoName)
        contributorAdapter = ContributorAdapter()
        setupObserver()
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.repoDetails.collect {
                when (it) {
                    is Resource.EmptyData -> {}
                    is Resource.Error -> binding.progressBar.isVisible = false
                    is Resource.Loading -> binding.progressBar.isVisible = true
                    is Resource.Success -> {
                        setupViews(it.dataFetched)
                    }
                }
            }
        }
    }

    private fun setupViews(repo: Repo) {
        binding.apply {
            progressBar.isVisible = false
            binding.detailsContainer.isVisible = true
            repositoryLogo.load(repo.owner.avatarUrl)
            val name = "@${repo.owner.login}"
            ownerName.text = name
            repoName.text = repo.name
            repoDesc.text = repo.description
            starCount.text = repo.stars.toString()
            forkCount.text = repo.forks.toString()
            contributorRecyclerview.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = contributorAdapter
            }
            contributorAdapter.setContributors(repo.contributorList?: emptyList())
            openProjectButton.setOnClickListener {
                loadUrlInWebView(repo.htmlUrl)
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadUrlInWebView(htmlUrl: String) {
        binding.apply {
            webview.isVisible = true
            detailsContainer.isVisible = false
            progressBar.isVisible = true
            webview.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    progressBar.isVisible = false
                }
            }
            webview.settings.javaScriptEnabled = true
            webview.loadUrl(htmlUrl)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}