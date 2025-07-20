package com.emon.mycontactapp.domain.model

import java.io.Serializable

data class ContactList(
    val result: List<Contact> = emptyList()
): Serializable

data class Contact(
    val email: String,
    val fullName: String,
    val imageUrl: String,
    val phoneNumber: String
): Serializable
