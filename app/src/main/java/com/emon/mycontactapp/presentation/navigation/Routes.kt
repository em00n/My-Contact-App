package com.emon.mycontactapp.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
object ContactListRoute

@Serializable
data class ContactDetailsRoute(val id: Int)
