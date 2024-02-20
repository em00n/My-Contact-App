package com.emon.mycontactapp.model

data class ContactListApiResponse(
    val error: Error,
    val result: List<ContactListResult>,
    val status: Boolean
)

data class Error(
    val code: Int?,
    val message: String?
)

data class ContactListResult(
    val email: String,
    val full_name: String,
    val image: String,
    val phone_number: String
)