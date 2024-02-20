package com.emon.mycontactapp.utils


sealed interface Result<out R> {
    data class Loading<out T>(val loading: Boolean) : Result<T>
    data class Success<out T>(val data: T) : Result<T>
    data class Error<out T>(val message: String, val code: Int) : Result<T>
}