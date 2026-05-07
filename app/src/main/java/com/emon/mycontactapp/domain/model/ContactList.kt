package com.emon.mycontactapp.domain.model


import kotlinx.serialization.Serializable

@Serializable
data class ContactList(
    val result: List<Contact> = emptyList()
)

@Serializable
data class Contact(
    val id: Int,
    val email: String,
    val fullName: String,
    val imageUrl: String,
    val phoneNumber: String
)
