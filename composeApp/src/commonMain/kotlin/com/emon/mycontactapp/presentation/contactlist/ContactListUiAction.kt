package com.emon.mycontactapp.presentation.contactlist

sealed interface ContactListUiAction {

    data object FetchContacts : ContactListUiAction

    data class OnSearchQueryChange(
        val query: String
    ) : ContactListUiAction

    data object Retry : ContactListUiAction
}