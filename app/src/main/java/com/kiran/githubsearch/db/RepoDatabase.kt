package com.kiran.githubsearch.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kiran.githubsearch.data.models.Repo

@Database(entities = [Repo::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class RepoDatabase : RoomDatabase() {
    abstract fun repoDao(): RepoDataDAo
}