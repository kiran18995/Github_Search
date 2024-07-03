package com.kiran.githubsearch.api

import com.kiran.githubsearch.BuildConfig

interface GithubApi {
    companion object {
        const val BASE_URL = BuildConfig.BASE_URL
    }
}