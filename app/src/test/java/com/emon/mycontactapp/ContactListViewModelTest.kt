package com.emon.mycontactapp

import com.emon.mycontactapp.data.repository.ContactListRepository
import com.emon.mycontactapp.model.ContactListApiResponse
import com.emon.mycontactapp.model.ContactListResult
import com.emon.mycontactapp.model.Error
import com.emon.mycontactapp.ui.ContactListUiAction
import com.emon.mycontactapp.ui.ContactListUiState
import com.emon.mycontactapp.ui.ContactListViewModel
import com.emon.mycontactapp.utils.Result
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
    private lateinit var contactListRepository: ContactListRepository
    private lateinit var viewModel: ContactListViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher()) // Set the main dispatcher for tests
        contactListRepository = mockk()
        viewModel = ContactListViewModel(contactListRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset the main dispatcher to the original dispatcher
    }

    @Test
    fun fetchContactListApiLoading() = runTest {
        // Mock loading state
        coEvery { contactListRepository.fetchContactList() } returns flowOf(Result.Loading(true))

        // Trigger action
        viewModel.action(ContactListUiAction.FetchContactListApi)

        // Simulate coroutine execution
        advanceUntilIdle() // Completes all pending coroutines

        // Verify loading state
        val state = viewModel.uiState.value
        assertTrue(state is ContactListUiState.Loading)
        assertTrue((state as ContactListUiState.Loading).isLoading)
    }

    @Test
    fun fetchContactListApiSuccess() = runTest {
        // Mock repository success response
        //val mockResult = mockk<ContactListApiResponse>(relaxed = true)
        val mockResult = listOf<ContactListResult>()
        val mockResponse = ContactListApiResponse(
            error = null, // Ensure no error
            result = mockResult,
            status = true // Indicates success
        )
        coEvery { contactListRepository.fetchContactList() } returns flowOf(
            Result.Success(
                mockResponse
            )
        )

        // Trigger action
        viewModel.action(ContactListUiAction.FetchContactListApi)

        // Simulate coroutine execution
        advanceUntilIdle() // Completes all pending coroutines

        // Verify Api Success
        val state = viewModel.uiState.value
        assertTrue(state is ContactListUiState.ContactListApiSuccess)
        assertEquals(mockResult, (state as ContactListUiState.ContactListApiSuccess).data)

    }

    @Test
    fun fetchContactListApiError() = runTest {
        // Mock repository error response
        coEvery { contactListRepository.fetchContactList() } returns flowOf(
            Result.Error(
                message = "Error",
                code = 400
            )
        )

        // Trigger action
        viewModel.action(ContactListUiAction.FetchContactListApi)

        // Simulate coroutine execution
        advanceUntilIdle() // Completes all pending coroutines

        // Verify Api Error
        val state = viewModel.uiState.value
        assertTrue(state is ContactListUiState.ApiError)
        assertEquals("Error", (state as ContactListUiState.ApiError).message)
    }

    @Test
    fun fetchContactListApiErrorInResponse() = runTest {
        // Mock repository error response
        val mockResult = mockk<List<ContactListResult>>(relaxed = true)
        val mockResponse = ContactListApiResponse(
            error = Error(500, "Error"), // Ensure error
            result = mockResult,
            status = false // Indicates error
        )
        coEvery { contactListRepository.fetchContactList() } returns flowOf(
            Result.Success(
                mockResponse
            )
        )

        // Trigger action
        viewModel.action(ContactListUiAction.FetchContactListApi)

        // Simulate coroutine execution
        advanceUntilIdle() // Completes all pending coroutines

        // Verify Api Error in response
        val state = viewModel.uiState.value
        assertTrue(state is ContactListUiState.ApiError)
        assertEquals("Error", (state as ContactListUiState.ApiError).message)
    }

}


