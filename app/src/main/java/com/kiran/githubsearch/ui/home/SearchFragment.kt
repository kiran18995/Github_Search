package com.kiran.githubsearch.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.kiran.githubsearch.databinding.FragmentSearchBinding
import com.kiran.githubsearch.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<SearchViewModel>()
    private lateinit var adapter: SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        adapter = SearchAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupObserver()
        setupSearchView()
    }

    private fun setupViews() {
        binding.apply {
            recyclerview.layoutManager = LinearLayoutManager(requireContext())
            recyclerview.adapter = adapter
        }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (it.isNotEmpty()) {
                        showProgressBar()
                        adapter.submitData(lifecycle, PagingData.empty())
                        viewModel.setQuery(it)
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // You can handle text change here if needed
                return false
            }
        })
    }

    private fun showProgressBar() {
        binding.progressBar.isVisible = true
        binding.searchWelcomeMessage.visibility = View.GONE
    }

    private fun hideProgressBar() {
        binding.progressBar.isVisible = false
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.repoList.collect {
                when (it) {
                    is Resource.Error -> {
                        hideProgressBar()
                        binding.searchWelcomeMessage.visibility = View.GONE
                    }

                    is Resource.Loading -> {
                        showProgressBar()
                    }

                    is Resource.Success -> {
                        hideProgressBar()
                        adapter.submitData(it.dataFetched)
                    }

                    is Resource.EmptyData -> {
                        binding.searchWelcomeMessage.visibility = View.VISIBLE
                        hideProgressBar()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}