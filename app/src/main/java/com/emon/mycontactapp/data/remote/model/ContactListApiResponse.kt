package com.emon.mycontactapp.data.remote.model

import java.io.Serializable

data class ContactListApiResponse(
    val result: List<ContactListResult>? = listOf(),
): Serializable

data class ContactListResult(
    val id: Int?,
    val email: String?,
    val full_name: String?,
    val image: String?,
    val phone_number: String?
) : Serializable