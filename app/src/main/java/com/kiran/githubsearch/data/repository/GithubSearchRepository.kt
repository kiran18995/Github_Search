package com.kiran.githubsearch.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kiran.githubsearch.api.GithubApi
import com.kiran.githubsearch.data.models.Repo
import com.kiran.githubsearch.data.paging.GithubSearchDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GithubSearchRepository @Inject constructor(private val githubApi: GithubApi) {
    fun searchRepos(query: String): Flow<PagingData<Repo>> {
        return Pager(config = PagingConfig(
            pageSize = 2, enablePlaceholders = false, prefetchDistance = 20,
        ), pagingSourceFactory = { GithubSearchDataSource(githubApi, query, 10) }).flow
    }
}