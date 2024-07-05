package com.kiran.githubsearch.ui.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiran.githubsearch.data.models.Repo
import com.kiran.githubsearch.data.repository.GithubSearchRepository
import com.kiran.githubsearch.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepositoryViewModel @Inject constructor(
    private val repository: GithubSearchRepository
) : ViewModel() {
    private val _repoDetails = MutableStateFlow<Resource<Repo>>(Resource.Loading())
    val repoDetails get() = _repoDetails

    fun getQuotesGenres(owner: String, name: String) = viewModelScope.launch {
        repository.getRepoDetails(owner, name).collect {
            _repoDetails.emit(it)
        }
    }
}