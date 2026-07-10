package com.emon.mycontactapp.core.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun <R, E> Flow<Resource<R>>.mapResponseWith(mapper: Mapper<R, E>): Flow<Resource<E>> {
    return map {
        when (it) {
            is Resource.Success -> Resource.Success(mapper.mapFromApiResponse(it.data))
            is Resource.Error -> Resource.Error(it.message, it.code)
            is Resource.Loading -> Resource.Loading
        }
    }
}
