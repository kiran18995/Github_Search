package com.kiran.githubsearch.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kiran.githubsearch.data.models.Repo
import com.kiran.githubsearch.data.repository.GithubSearchRepository
import com.kiran.githubsearch.di.AppModule.NetworkChecker
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
    private val repository: GithubSearchRepository,
    private val networkChecker: NetworkChecker
) : ViewModel() {
    private val _repoList = MutableStateFlow<Resource<PagingData<Repo>>>(Resource.Loading())
    val repoList: MutableStateFlow<Resource<PagingData<Repo>>> = _repoList

    private val currentQuery = MutableStateFlow("")

    init {
        searchRepos()
    }

    fun setQuery(query: String) {
        currentQuery.value = query
    }

    @OptIn(FlowPreview::class)
    private fun searchRepos() {
        viewModelScope.launch {
            currentQuery.debounce(300) // Debounce to avoid rapid API calls
                .distinctUntilChanged().collectLatest { query ->
                    _repoList.value = Resource.Loading()
                    if (query.isEmpty() && networkChecker.isNetworkConnected()) {
                        _repoList.value = Resource.EmptyData()
                        return@collectLatest
                    }
                    try {
                        repository.searchRepos(query).cachedIn(viewModelScope)
                            .collectLatest { pagingData ->
                                _repoList.value = Resource.Success(pagingData)
                            }
                    } catch (e: Exception) {
                        _repoList.value = Resource.Error(e)
                    }
                }
        }
    }
}