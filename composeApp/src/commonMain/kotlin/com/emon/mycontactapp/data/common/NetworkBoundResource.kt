package com.emon.mycontactapp.data.common

import com.emon.mycontactapp.core.utils.Resource
import io.github.aakira.napier.Napier
import io.ktor.client.plugins.ResponseException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * Wraps a suspending Ktor call into a [Flow] of [Resource], emitting Loading → Success/Error.
 * Ktor already deserializes the body, so this takes the deserialized type directly (no Retrofit
 * `Response<T>`). `Dispatchers.IO` is JVM/Native-only, so commonMain uses `Dispatchers.Default`.
 */
class NetworkBoundResource {

    fun <T> performApiRequest(
        apiCall: suspend () -> T
    ): Flow<Resource<T>> = flow {
        emit(Resource.Loading)
        emit(Resource.Success(data = apiCall()))
    }.catch { error ->
        Napier.e(throwable = error) { "API request failed" }
        emit(Resource.Error(message = getErrorMessage(error), code = getErrorCode(error)))
    }.flowOn(Dispatchers.Default)

    private fun getErrorMessage(throwable: Throwable): String {
        return when (throwable) {
            is ResponseException -> "Whoops! Something went wrong. Please try again."
            else -> "Whoops! No Internet Connection. Please try again."
        }
    }

    private fun getErrorCode(throwable: Throwable): Int {
        return (throwable as? ResponseException)?.response?.status?.value ?: 0
    }
}
