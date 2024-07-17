package com.kiran.githubsearch.utils

import com.kiran.githubsearch.data.models.ApiErrorResponse
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException

const val THROWABLE_API_ERROR_TAG = "THROWABLE_API_ERROR"

class NoConnectivityException : IOException() {
    override val message: String = "No internet connection"
}

fun Throwable.toApiErrorResponse(): ApiErrorResponse {
    val defaultResponse = ApiErrorResponse()
    try {
        return when (this) {
            is ConnectException ->
                return ApiErrorResponse(ApiErrorResponse.Status.REMOTE_CONNECTION_ERROR, 0)

            is NoConnectivityException ->
                return ApiErrorResponse(ApiErrorResponse.Status.NETWORK_CONNECTION_ERROR, 0)

            is HttpException -> {
                val error = Moshi.Builder()
                    .build()
                    .adapter(ApiErrorResponse::class.java)
                    .fromJson(this.response()?.errorBody()?.string().orEmpty())

                if (error != null)
                    ApiErrorResponse(
                        ApiErrorResponse.Status[this.code()],
                        error.statusCode,
                        error.message
                    )
                else
                    defaultResponse
            }

            else -> {
                val message = this.message
                if (message != null && message.contains("unexpected end of stream"))
                    return ApiErrorResponse(ApiErrorResponse.Status.REMOTE_CONNECTION_ERROR, 0)
                return defaultResponse
            }
        }
    } catch (e: IOException) {
        Logger.e(THROWABLE_API_ERROR_TAG, e.toString())
    } catch (e: JsonDataException) {
        Logger.e(THROWABLE_API_ERROR_TAG, e.toString())
    } catch (e: NullPointerException) {
        Logger.e(THROWABLE_API_ERROR_TAG, e.toString())
    }
    return defaultResponse
}