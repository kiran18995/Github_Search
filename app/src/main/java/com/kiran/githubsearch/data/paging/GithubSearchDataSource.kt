package com.kiran.githubsearch.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kiran.githubsearch.api.GithubApi
import com.kiran.githubsearch.data.models.Repo

class GithubSearchDataSource(
    private val githubApi: GithubApi, private val query: String, private val perPage: Int
) : PagingSource<Int, Repo>() {

    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, Repo>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repo> {
        try {
            val position = params.key ?: STARTING_PAGE_INDEX
            val response = githubApi.searchRepos(query, perPage)
            return LoadResult.Page(
                data = response.items,
                prevKey = null,
                nextKey = if (response.items.isEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}