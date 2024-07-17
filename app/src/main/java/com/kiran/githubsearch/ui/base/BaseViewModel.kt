package com.kiran.githubsearch.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiran.githubsearch.R
import com.kiran.githubsearch.data.models.ApiErrorResponse
import com.kiran.githubsearch.utils.Loader
import com.kiran.githubsearch.utils.Logger
import com.kiran.githubsearch.utils.Message
import com.kiran.githubsearch.utils.Messenger
import com.kiran.githubsearch.utils.toApiErrorResponse
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class BaseViewModel(
    private val loader: Loader, private val messenger: Messenger
) : ViewModel() {

    companion object {
        const val TAG = "BaseViewModel"
    }

    protected fun launchNetwork(
        silent: Boolean = false,
        error: (ApiErrorResponse) -> Unit = {},
        block: suspend CoroutineScope.() -> Unit
    ) {
        if (!silent) {
            viewModelScope.launch {
                try {
                    block()
                } catch (e: Throwable) {
                    if (e is CancellationException) return@launch
                    val errorResponse = e.toApiErrorResponse()
                    handleNetworkError(errorResponse)
                    error(errorResponse)
                    Logger.d(TAG, e)
                } finally {

                }
            }
        } else {
            viewModelScope.launch {
                try {
                    block()
                } catch (e: Throwable) {
                    if (e is CancellationException) return@launch
                    val errorResponse = e.toApiErrorResponse()
                    error(errorResponse)
                    Logger.d(TAG, e)
                }
            }
        }
    }

    private fun handleNetworkError(err: ApiErrorResponse) {
        when (err.status) {
            ApiErrorResponse.Status.HTTP_BAD_GATEWAY, ApiErrorResponse.Status.REMOTE_CONNECTION_ERROR -> {
                messenger.deliverRes(Message.error(R.string.server_connection_error))
//                navigator.navigateTo(Destination.ServerUnreachable.route)
            }

            ApiErrorResponse.Status.NETWORK_CONNECTION_ERROR -> messenger.deliverRes(Message.error(R.string.no_internet_connection))

            ApiErrorResponse.Status.HTTP_UNAUTHORIZED -> messenger.deliver(Message.error(err.message))

            ApiErrorResponse.Status.HTTP_FORBIDDEN -> messenger.deliverRes(Message.error(R.string.permission_not_available))

            ApiErrorResponse.Status.HTTP_BAD_REQUEST -> err.message.let {
                messenger.deliver(
                    Message.error(
                        err.message
                    )
                )
            }

            ApiErrorResponse.Status.HTTP_NOT_FOUND -> err.message.let {
                messenger.deliver(
                    Message.error(
                        err.message
                    )
                )
            }

            ApiErrorResponse.Status.HTTP_INTERNAL_ERROR -> messenger.deliverRes(Message.error(R.string.network_internal_error))

            ApiErrorResponse.Status.HTTP_UNAVAILABLE -> {
                messenger.deliverRes(Message.error(R.string.network_server_not_available))
//                navigator.navigateTo(Destination.ServerUnreachable.route)
            }

            ApiErrorResponse.Status.UNKNOWN -> messenger.deliverRes(Message.error(R.string.something_went_wrong))
        }
    }
}