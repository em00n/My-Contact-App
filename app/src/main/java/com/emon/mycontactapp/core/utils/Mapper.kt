package com.emon.mycontactapp.core.utils

interface Mapper<R, E> {
    fun mapFromApiResponse(type: R): E
}