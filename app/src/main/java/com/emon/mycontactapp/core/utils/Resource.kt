package com.emon.mycontactapp.core.utils


sealed interface Resource<out R> {
    data class Loading<out T>(val loading: Boolean) : Resource<T>
    data class Success<out T>(val data: T) : Resource<T>
    data class Error<out T>(val message: String, val code: Int) : Resource<T>
}