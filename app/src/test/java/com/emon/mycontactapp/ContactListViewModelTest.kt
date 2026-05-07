package com.emon.mycontactapp

import com.emon.mycontactapp.core.utils.Resource
import com.emon.mycontactapp.domain.model.Contact
import com.emon.mycontactapp.domain.model.ContactList
import com.emon.mycontactapp.domain.usecase.GetContactListUseCase
import com.emon.mycontactapp.presentation.contactlist.ContactListUiAction
import com.emon.mycontactapp.presentation.contactlist.ContactListUiState
import com.emon.mycontactapp.presentation.contactlist.ContactListViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ContactListViewModelTest {

    // Mocks
    private lateinit var getContactListUseCase: GetContactListUseCase
    private lateinit var viewModel: ContactListViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher()) // Set the main dispatcher for tests
        getContactListUseCase = mockk()
        viewModel = ContactListViewModel(getContactListUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset the main dispatcher to the original dispatcher
    }

    @Test
    fun fetchContactListApiLoading() = runTest {
        // Mock loading state
        coEvery { getContactListUseCase.invoke() } returns flowOf(Resource.Loading(true))

        // Trigger action
        viewModel.action(ContactListUiAction.FetchContacts)

        // Simulate coroutine execution
        advanceUntilIdle() // Completes all pending coroutines

        // Verify loading state
        val state = viewModel.uiState.value
        assertTrue(state is ContactListUiState.Loading)
    }

    @Test
    fun fetchContactListApiSuccess() = runTest {
        // Mock repository success response
        val mockResult = listOf(mockk<Contact>(relaxed = true))
        //val mockResult = listOf<Contact>()
        val mockResponse = ContactList(
            result = mockResult
        )
        coEvery { getContactListUseCase.invoke() } returns flowOf(
            Resource.Success(
                mockResponse
            )
        )

        // Trigger action
        viewModel.action(ContactListUiAction.FetchContacts)

        // Simulate coroutine execution
        advanceUntilIdle() // Completes all pending coroutines

        // Verify Api Success
        val state = viewModel.uiState.value
        assertTrue(state is ContactListUiState.Success)
        assertEquals(mockResult, (state as ContactListUiState.Success).contacts)

    }

    @Test
    fun fetchContactListApiError() = runTest {
        // Mock repository error response
        coEvery { getContactListUseCase.invoke() } returns flowOf(
            Resource.Error(
                message = "Error",
                code = 400
            )
        )

        // Trigger action
        viewModel.action(ContactListUiAction.FetchContacts)

        // Simulate coroutine execution
        advanceUntilIdle() // Completes all pending coroutines

        // Verify Api Error
        val state = viewModel.uiState.value
        assertTrue(state is ContactListUiState.Error)
        assertEquals("Error", (state as ContactListUiState.Error).message)
    }

    @Test
    fun fetchContactListEmptyList() = runTest {
        // Mock repository success response with empty list
        val mockResponse = ContactList(result = emptyList())
        coEvery { getContactListUseCase.invoke() } returns flowOf(Resource.Success(mockResponse))

        // Trigger action
        viewModel.action(ContactListUiAction.FetchContacts)
        advanceUntilIdle()

        // Verify Api Success with empty list
        val state = viewModel.uiState.value
        assertTrue(state is ContactListUiState.Success)
        assertTrue((state as ContactListUiState.Success).contacts.isEmpty())
    }

    @Test
    fun fetchContactListUnexpectedException() = runTest {
        // Mock repository throws exception
        coEvery { getContactListUseCase.invoke() } returns flowOf(Resource.Error(message = "Unexpected error", code = 404))

        // Trigger action
        viewModel.action(ContactListUiAction.FetchContacts)
        advanceUntilIdle()

        // Verify Api Error
        val state = viewModel.uiState.value
        assertTrue(state is ContactListUiState.Error)
        assertEquals("Unexpected error", (state as ContactListUiState.Error).message)
    }

    @Test
    fun repeatedFetchContactListStateConsistency() = runTest {
        // Mock repository success response
        val mockResult = listOf(mockk<Contact>(relaxed = true))
        val mockResponse = ContactList(result = mockResult)
        coEvery { getContactListUseCase.invoke() } returns flowOf(Resource.Success(mockResponse))

        // Trigger action multiple times
        viewModel.action(ContactListUiAction.FetchContacts)
        viewModel.action(ContactListUiAction.FetchContacts)
        advanceUntilIdle()

        // Verify state is still success and data is correct
        val state = viewModel.uiState.value
        assertTrue(state is ContactListUiState.Success)
        assertEquals(mockResult, (state as ContactListUiState.Success).contacts)
    }
}
