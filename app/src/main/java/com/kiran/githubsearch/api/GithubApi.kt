package com.kiran.githubsearch.api

import com.kiran.githubsearch.BuildConfig
import com.kiran.githubsearch.data.models.Repo
import com.kiran.githubsearch.data.models.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApi {
    companion object {
        const val BASE_URL = BuildConfig.BASE_URL
    }

    @GET("search/repositories")
    suspend fun searchRepos(
        @Query("q") query: String, @Query("per_page") perPage: Int, @Query("page") page: Int
    ): SearchResponse

    @GET("repos/{owner}/{name}")
    suspend fun getRepo(
        @Path("owner") owner: String, @Path("name") name: String
    ): Repo
}