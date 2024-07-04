@file:OptIn(FlowPreview::class)

package com.kiran.githubsearch.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.kiran.githubsearch.data.models.Repo
import com.kiran.githubsearch.data.repository.GithubSearchRepository
import com.kiran.githubsearch.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: GithubSearchRepository
) : ViewModel() {
    private val _repoList = MutableStateFlow<Resource<PagingData<Repo>>>(Resource.Loading())
    val repoList: MutableStateFlow<Resource<PagingData<Repo>>> = _repoList

    private val currentQuery = MutableStateFlow("")

    init {
        viewModelScope.launch {
            currentQuery
                .debounce(300) // Optional: debounce to prevent rapid queries
                .distinctUntilChanged()
                .collectLatest { query ->
                    searchRepos(query)
                }
        }
    }

    fun setQuery(query: String) {
        currentQuery.value = query
    }

    private fun searchRepos(query: String) {
        viewModelScope.launch {
            try {
                repository.searchRepos(query)
                    .collectLatest { pagingData ->
                        _repoList.value = Resource.Success(pagingData)
                    }
            } catch (e: Exception) {
                _repoList.value = Resource.Error(e)
            }
        }
    }
}