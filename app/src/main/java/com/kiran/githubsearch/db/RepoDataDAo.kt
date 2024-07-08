package com.kiran.githubsearch.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kiran.githubsearch.data.models.Repo

@Dao
interface RepoDataDAo {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepo(repo: Repo): Long

    @Query("SELECT * FROM RepoDatabase")
    suspend fun getRepos(): List<Repo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepos(repos: List<Repo>): List<Long> {
        var currentCount = getRepoCount()
        if (currentCount >= 15) {
            deleteOldRepos(currentCount)
            currentCount = 0
        }
        val toInsert = repos.take(15 - currentCount)
        return if (toInsert.isNotEmpty()) {
            insertReposInternal(toInsert)
        } else {
            emptyList()
        }
    }

    @Query("DELETE FROM RepoDatabase WHERE id IN (SELECT id FROM RepoDatabase ORDER BY id ASC LIMIT :count)")
    suspend fun deleteOldRepos(count: Int)

    @Query("SELECT COUNT(*) FROM RepoDatabase")
    suspend fun getRepoCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReposInternal(repos: List<Repo>): List<Long>
}