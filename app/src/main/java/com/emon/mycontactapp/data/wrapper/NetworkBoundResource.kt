package com.emon.mycontactapp.data.wrapper

import com.emon.mycontactapp.utils.Result
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

class NetworkBoundResource @Inject constructor() {
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    suspend fun <ResultType> downloadData(api: suspend () -> Response<ResultType>): Flow<Result<ResultType>> {
        return withContext(ioDispatcher) {
            flow {
                emit(Result.Loading(true))
                val response: Response<ResultType> = api()
                emit(Result.Loading(false))
                if (response.isSuccessful) {
                    response.body()?.let {
                        emit(Result.Success(data = it))
                    } ?: emit(Result.Error(message = "Unknown error occurred", code = 0))
                } else {
                    emit(
                        Result.Error(
                            message = parseErrorBody(response.errorBody()),
                            code = response.code()
                        )
                    )
                }
            }.catch { error ->
                Timber.e(error.localizedMessage)
                emit(Result.Loading(false))
                emit(Result.Error(message = getErrorMessage(error), code = getErrorCode(error)))
            }
        }
    }

    private fun parseErrorBody(response: ResponseBody?): String {
        return response?.let {
            try {
                val errorMessage =
                    JsonParser.parseString(it.string()).asJsonObject["message"].asString
                if (errorMessage.isNotEmpty()) {
                    return@let errorMessage
                }
            } catch (e: Exception) {
                Timber.e(e, "Error parsing error body")
            }
            return@let "Whoops! Something went wrong. Please try again."
        } ?: "Whoops! Unknown error occurred. Please try again."
    }

    private fun getErrorMessage(throwable: Throwable?): String {
        return when (throwable) {
            is SocketTimeoutException -> "Whoops! Connection timed out. Please try again."
            is IOException -> "Whoops! No Internet Connection. Please try again."
            is HttpException -> {
                try {
                    val errorBody = throwable.response()?.errorBody()?.string()
                    val errorMessage =
                        JsonParser.parseString(errorBody).asJsonObject["message"].asString
                    if (errorMessage.isNotEmpty()) {
                        return errorMessage
                    }
                } catch (e: Exception) {
                    Timber.e(e, "Error parsing HTTP exception")
                }
                "Whoops! Something went wrong. Please try again."
            }

            else -> "Whoops! Unknown error occurred. Please try again."
        }
    }

    private fun getErrorCode(throwable: Throwable?): Int {
        return if (throwable is HttpException) throwable.code() else 0
    }
}