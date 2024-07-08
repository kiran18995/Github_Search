package com.kiran.githubsearch.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kiran.githubsearch.data.models.Contributor
import com.kiran.githubsearch.data.models.Owner

class Converters {
    @TypeConverter
    fun fromContributorList(contributorList: List<Contributor>?): String? {
        return Gson().toJson(contributorList)
    }

    @TypeConverter
    fun toContributorList(contributorListString: String?): List<Contributor>? {
        return if (contributorListString == null) {
            emptyList()
        } else {
            val listType = object : TypeToken<List<Contributor>>() {}.type
            Gson().fromJson(contributorListString, listType)
        }
    }

    @TypeConverter
    fun fromOwner(owner: Owner?): String? {
        return Gson().toJson(owner)
    }

    @TypeConverter
    fun toOwner(ownerString: String?): Owner? {
        return Gson().fromJson(ownerString, Owner::class.java)
    }
}