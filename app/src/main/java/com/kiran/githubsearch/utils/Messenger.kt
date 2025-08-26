package com.kiran.githubsearch.utils

import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@ActivityRetainedScoped
class Messenger @Inject constructor() {
    private val _message = MutableSharedFlow<Message<String>>(extraBufferCapacity = 1)

    private val _messageRes = MutableSharedFlow<Message<Int>>(extraBufferCapacity = 1)

    private val _clear = MutableSharedFlow<Boolean>(extraBufferCapacity = 1)

    private val _messageType = MutableStateFlow(Message.Type.SUCCESS) // Need initial state


    fun deliver(message: Message<String>) {
        _clear.tryEmit(true)
        _messageType.tryEmit(message.type)
        _message.tryEmit(message)
    }

    fun deliverRes(message: Message<Int>) {
        _clear.tryEmit(true)
        _messageType.tryEmit(message.type)
        _messageRes.tryEmit(message)
    }
}