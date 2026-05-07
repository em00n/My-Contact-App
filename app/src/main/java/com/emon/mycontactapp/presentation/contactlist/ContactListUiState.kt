package com.emon.mycontactapp.presentation.contactlist

import com.emon.mycontactapp.domain.model.Contact

sealed interface ContactListUiState {

    data object Loading : ContactListUiState

    data class Success(
        val contacts: List<Contact>,
        val searchQuery: String = ""
    ) : ContactListUiState

    data class Error(
        val message: String
    ) : ContactListUiState
}