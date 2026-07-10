package com.emon.mycontactapp.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ContactListApiResponse(
    val result: List<ContactListResult>? = emptyList()
)

@Serializable
data class ContactListResult(
    val id: Int? = null,
    val email: String? = null,
    @SerialName("full_name") val fullName: String? = null,
    val image: String? = null,
    @SerialName("phone_number") val phoneNumber: String? = null
)
