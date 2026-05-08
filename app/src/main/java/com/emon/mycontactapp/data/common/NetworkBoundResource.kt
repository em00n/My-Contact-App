package com.emon.mycontactapp.data.common

import com.emon.mycontactapp.core.utils.Resource
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

class NetworkBoundResource @Inject constructor() {

    fun <ResultType> performApiRequest(
        api: suspend () -> Response<ResultType>
    ): Flow<Resource<ResultType>> = flow {
        emit(Resource.Loading)
        val response: Response<ResultType> = api()
        if (response.isSuccessful) {
            response.body()?.let {
                emit(Resource.Success(data = it))
            } ?: emit(Resource.Error(message = "Unknown error occurred", code = 0))
        } else {
            emit(
                Resource.Error(
                    message = parseErrorBody(response.errorBody()),
                    code = response.code()
                )
            )
        }
    }.catch { error ->
        Timber.e(error.localizedMessage)
        emit(Resource.Error(message = getErrorMessage(error), code = getErrorCode(error)))
    }.flowOn(Dispatchers.IO)

    private fun parseErrorBody(response: ResponseBody?): String {
        return response?.let {
            try {
                val json = JsonParser.parseString(it.string()).asJsonObject
                val message = json["message"]?.asString
                if (!message.isNullOrEmpty()) return@let message
            } catch (e: Exception) {
                Timber.e(e, "Error parsing error body")
            }
            "Whoops! Something went wrong. Please try again."
        } ?: "Whoops! Unknown error occurred. Please try again."
    }

    private fun getErrorMessage(throwable: Throwable?): String {
        return when (throwable) {
            is SocketTimeoutException -> "Whoops! Connection timed out. Please try again."
            is IOException -> "Whoops! No Internet Connection. Please try again."
            is HttpException -> parseErrorBody(throwable.response()?.errorBody())
            else -> "Whoops! Unknown error occurred. Please try again."
        }
    }

    private fun getErrorCode(throwable: Throwable?): Int {
        return if (throwable is HttpException) throwable.code() else 0
    }
}