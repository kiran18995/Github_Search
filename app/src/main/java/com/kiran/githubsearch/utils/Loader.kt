package com.kiran.githubsearch.utils

import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

@ActivityRetainedScoped
class Loader @Inject constructor() {

    private val counter = AtomicInteger(0)

    private val _loading = MutableStateFlow(false)

    fun start() =
        counter.incrementAndGet().run { _loading.value = true }

}
