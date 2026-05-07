package com.emon.mycontactapp.presentation.contactlist

import com.emon.mycontactapp.core.base.BaseViewModel
import com.emon.mycontactapp.core.utils.Resource
import com.emon.mycontactapp.domain.model.Contact
import com.emon.mycontactapp.domain.usecase.GetContactListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ContactListViewModel @Inject constructor(
    private val getContactListUseCase: GetContactListUseCase
) : BaseViewModel() {

    private val _uiState =
        MutableStateFlow<ContactListUiState>(ContactListUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        fetchContacts()
    }

    fun action(action: ContactListUiAction) {
        when (action) {
            is ContactListUiAction.FetchContacts -> fetchContacts()
            is ContactListUiAction.OnSearchQueryChange -> {
                val currentState = _uiState.value
                if (currentState is ContactListUiState.Success) {
                    _uiState.update {
                        currentState.copy(searchQuery = action.query)
                    }
                }
            }

            is ContactListUiAction.Retry -> fetchContacts()
        }
    }

    private fun fetchContacts() {
        execute {
            getContactListUseCase.invoke().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.value = ContactListUiState.Loading
                    }
                    is Resource.Success -> {
                        _uiState.value = ContactListUiState.Success(result.data.result)
                    }

                    is Resource.Error -> {
                        _uiState.value = ContactListUiState.Error(result.message)
                    }
                }
            }
        }
    }

    fun getContact(id: Int): Contact? {
        return (_uiState.value as? ContactListUiState.Success)
            ?.contacts
            ?.firstOrNull { it.id == id }
    }
}