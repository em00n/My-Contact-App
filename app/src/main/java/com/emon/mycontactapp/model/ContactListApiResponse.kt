package com.emon.mycontactapp.model

data class ContactListApiResponse(
    val error: Error,
    val result: List<Result>,
    val status: Boolean
)

data class Error(
    val code: Any,
    val message: Any
)

data class Result(
    val email: String,
    val full_name: String,
    val image: String,
    val phone_number: String
)