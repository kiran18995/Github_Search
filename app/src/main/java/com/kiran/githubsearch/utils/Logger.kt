package com.kiran.githubsearch.utils

import com.kiran.githubsearch.BuildConfig
import timber.log.Timber

object Logger {

    init {
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

    fun d(tag: String, throwable: Throwable, vararg params: Any) =
        Timber.tag(tag).d(throwable, tag, params)

    fun e(tag: String, s: String, vararg params: Any) =
        Timber.tag(tag).e(s, params)

}