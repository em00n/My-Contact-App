package com.emon.mycontactapp.presentation.contactlist

import com.emon.mycontactapp.core.base.BaseViewModel
import com.emon.mycontactapp.core.utils.Resource
import com.emon.mycontactapp.domain.model.Contact
import com.emon.mycontactapp.domain.usecase.GetContactListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ContactListViewModel @Inject constructor(
    private val getContactListUseCase: GetContactListUseCase
) : BaseViewModel() {

    val action: (ContactListUiAction) -> Unit = {
        when (it) {
            is ContactListUiAction.FetchContactListApi -> fetchContactListApi()
            is ContactListUiAction.ResetUiState -> _uiState.value = ContactListUiState.Initial
        }
    }

    private val _uiState =
        MutableStateFlow<ContactListUiState<Any>>(ContactListUiState.Initial)
    val uiState get() = _uiState

    private fun fetchContactListApi() {
        execute {
            getContactListUseCase.invoke().collect { result ->
                when (result) {
                    is Resource.Loading -> _uiState.value = ContactListUiState.Loading(result.loading)
                    is Resource.Success -> {
                        _uiState.value =
                            ContactListUiState.ContactListApiSuccess(result.data.result)
                    }
                    is Resource.Error -> _uiState.value = ContactListUiState.ApiError(result.message)
                }
            }
        }
    }
}

sealed interface ContactListUiState<out R> {
    data object Initial : ContactListUiState<Initial>
    data class Loading(val isLoading: Boolean) : ContactListUiState<Loading>
    data class ContactListApiSuccess(val data: List<Contact>) : ContactListUiState<ContactListApiSuccess>
    data class ApiError(val message: String) : ContactListUiState<ApiError>
}

sealed interface ContactListUiAction {
    data object FetchContactListApi : ContactListUiAction
    data object ResetUiState : ContactListUiAction
}