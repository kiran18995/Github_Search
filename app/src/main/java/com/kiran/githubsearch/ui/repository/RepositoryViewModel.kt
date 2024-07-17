package com.kiran.githubsearch.ui.repository

import com.kiran.githubsearch.data.models.Repo
import com.kiran.githubsearch.data.repository.GithubSearchRepository
import com.kiran.githubsearch.ui.base.BaseViewModel
import com.kiran.githubsearch.utils.Loader
import com.kiran.githubsearch.utils.Messenger
import com.kiran.githubsearch.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class RepositoryViewModel @Inject constructor(
    loader: Loader, messenger: Messenger, private val repository: GithubSearchRepository
) : BaseViewModel(loader, messenger) {
    private val _repoDetails = MutableStateFlow<Resource<Repo>>(Resource.Loading())
    val repoDetails get() = _repoDetails

    fun getQuotesGenres(owner: String, name: String) = launchNetwork {
        repository.getRepoDetails(owner, name).collect {
            _repoDetails.emit(it)
        }
    }
}