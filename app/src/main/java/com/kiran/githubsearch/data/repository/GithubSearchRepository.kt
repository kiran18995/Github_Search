package com.kiran.githubsearch.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kiran.githubsearch.api.GithubApi
import com.kiran.githubsearch.data.models.Repo
import com.kiran.githubsearch.data.paging.GithubSearchDataSource
import com.kiran.githubsearch.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GithubSearchRepository @Inject constructor(private val githubApi: GithubApi) {
    fun searchRepos(query: String): Flow<PagingData<Repo>> {
        return Pager(config = PagingConfig(
            pageSize = 1, enablePlaceholders = false, prefetchDistance = 20,
        ), pagingSourceFactory = { GithubSearchDataSource(githubApi, query, 10) }).flow
    }

    suspend fun getRepoDetails(owner: String, name: String) = flow {
        try {
            val repoDetails = githubApi.getRepo(owner, name)
            repoDetails.contributorList = githubApi.getContributors(owner, name)
            if (repoDetails.name.isNotEmpty()) {
                emit(Resource.Success(repoDetails))
            } else {
                emit(Resource.Error(Throwable("Unable to Make Request")))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e))
        }
    }
}