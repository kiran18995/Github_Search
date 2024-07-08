package com.kiran.githubsearch.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kiran.githubsearch.api.GithubApi
import com.kiran.githubsearch.data.models.Repo
import com.kiran.githubsearch.db.RepoDatabase
import java.io.IOException

class GithubSearchDataSource(
    private val githubApi: GithubApi,
    private val query: String,
    private val perPage: Int,
    private val repoDatabase: RepoDatabase,
    private val isNetworkAvailable: () -> Boolean
) : PagingSource<Int, Repo>() {

    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, Repo>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repo> {
        return try {
            if (isNetworkAvailable()) {
                val position = params.key ?: STARTING_PAGE_INDEX
                val response = githubApi.searchRepos(query, perPage, position)
                if (position == 1 || position == 2 && response.items.isNotEmpty()) {
                    repoDatabase.repoDao().insertRepos(response.items)
                }
                LoadResult.Page(
                    data = response.items,
                    prevKey = null,
                    nextKey = if (response.items.isEmpty()) null else position + 1
                )
            } else {
                val repos = repoDatabase.repoDao().getRepos()
                if (repos.isEmpty()) {
                    LoadResult.Error(IOException("No data available in local database"))
                } else {
                    LoadResult.Page(
                        data = repos,
                        prevKey = null,
                        nextKey = null
                    )
                }
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        } catch (e: IOException) {
            LoadResult.Error(e)
        }
    }
}