package com.emon.mycontactapp.ui

import com.emon.mycontactapp.base.BaseViewModel
import com.emon.mycontactapp.data.repository.ContactListRepository
import com.emon.mycontactapp.model.ContactListResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import com.emon.mycontactapp.utils.Result
import javax.inject.Inject

@HiltViewModel
class ContactListViewModel @Inject constructor(
    private val contactListRepository: ContactListRepository
) : BaseViewModel() {

    val action: (ContactListUiAction) -> Unit = {
        when (it) {
            is ContactListUiAction.FetchContactListApi -> fetchContactListApi()
        }
    }

    private val _uiState =
        MutableStateFlow<ContactListUiState<Any>>(ContactListUiState.Loading(true))
    val uiState get() = _uiState

    private fun fetchContactListApi() {
        execute {
            contactListRepository.fetchContactList().collect { result ->
                when (result) {
                    is Result.Loading -> _uiState.value = ContactListUiState.Loading(result.loading)
                    is Result.Success -> {
                        result.data.error.code?.let {
                            _uiState.value = ContactListUiState.ApiError(result.data.error.message?:"")
                            return@collect
                        }
                        _uiState.value = ContactListUiState.ContactListApiSuccess(result.data.result)
                    }
                    is Result.Error -> _uiState.value = ContactListUiState.ApiError(result.message)
                }
            }
        }
    }
}

sealed interface ContactListUiState<out R> {
    data class Loading(val isLoading: Boolean) : ContactListUiState<Loading>
    data class ContactListApiSuccess(val data: List<ContactListResult>) : ContactListUiState<ContactListApiSuccess>
    data class ApiError(val message: String) : ContactListUiState<ApiError>
}

sealed interface ContactListUiAction {
    data object FetchContactListApi : ContactListUiAction
}