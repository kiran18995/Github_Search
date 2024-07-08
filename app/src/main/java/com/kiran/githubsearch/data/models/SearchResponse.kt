package com.kiran.githubsearch.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "RepoDatabase")
data class Repo(
    @PrimaryKey(autoGenerate = true)
    val _id: Int? = null,
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("description") val description: String?,
    @SerializedName("html_url") val htmlUrl: String,
    @SerializedName("url") val apiUrl: String,
    @SerializedName("stargazers_count") val stars: Int,
    @SerializedName("subscribers_count") val watchers: Int?,
    @SerializedName("forks_count") val forks: Int?,
    @SerializedName("language") val language: String?,
    @SerializedName("homepage") val homepage: String?,
    @SerializedName("owner") val owner: Owner,
    var contributorList: List<Contributor>? = null
)

data class SearchResponse(
    @SerializedName("total_count") val totalCount: Int,
    @SerializedName("incomplete_results") val incompleteResults: Boolean,
    @SerializedName("items") val items: List<Repo>
)

data class Owner(
    @SerializedName("id") val ownerId: Long,
    @SerializedName("login") val login: String,
    @SerializedName("avatar_url") val avatarUrl: String,
    @SerializedName("repoId") var repoId: Long = 0L
)

data class Contributor(
    @SerializedName("login") val login: String,
    @SerializedName("avatar_url") val avatarUrl: String
)