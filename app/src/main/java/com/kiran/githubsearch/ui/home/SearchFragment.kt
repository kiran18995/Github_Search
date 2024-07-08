package com.kiran.githubsearch.ui.home

import android.os.Bundle
import android.util.Log
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
import com.kiran.githubsearch.R
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
    private var _query: String = ""

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
                    if (it.isNotEmpty() && _query != query) {
                        showProgressBar()
                        adapter.submitData(lifecycle, PagingData.empty())
                        viewModel.setQuery(it)
                        _query = query
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
                        Log.e("test error", "setupObserver: " + it.error)
                        binding.searchWelcomeMessage.visibility = View.VISIBLE
                        binding.searchWelcomeMessage.text =
                            resources.getString(R.string.loading_error_please_check_network_connection)
                    }

                    is Resource.Loading -> {
                        showProgressBar()
                    }

                    is Resource.Success -> {
                        hideProgressBar()
                        binding.searchWelcomeMessage.visibility = View.GONE
                        adapter.submitData(it.dataFetched)
                    }

                    is Resource.EmptyData -> {
                        hideProgressBar()
                        binding.searchWelcomeMessage.visibility = View.VISIBLE
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