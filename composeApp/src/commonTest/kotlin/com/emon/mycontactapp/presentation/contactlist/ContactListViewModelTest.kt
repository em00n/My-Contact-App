package com.emon.mycontactapp.presentation.contactlist

import app.cash.turbine.test
import com.emon.mycontactapp.core.utils.Resource
import com.emon.mycontactapp.domain.model.Contact
import com.emon.mycontactapp.domain.model.ContactList
import com.emon.mycontactapp.domain.repository.ContactListRepository
import com.emon.mycontactapp.domain.usecase.GetContactListUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * commonTest replacement for the old MockK-based Android unit test. Uses a hand-written fake
 * repository (MockK is JVM-only) so the test runs on every target.
 */
class ContactListViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    private val sampleContact = Contact(
        id = 1,
        email = "john@example.com",
        fullName = "John Doe",
        imageUrl = "https://img/1",
        phoneNumber = "+123"
    )

    private class FakeRepository(
        private val resource: Resource<ContactList>
    ) : ContactListRepository {
        override suspend fun fetchContactList(): Flow<Resource<ContactList>> = flow {
            emit(Resource.Loading)
            emit(resource)
        }
    }

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @AfterTest
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun emits_success_with_contacts() = runTest(dispatcher) {
        val repository = FakeRepository(Resource.Success(ContactList(listOf(sampleContact))))
        val viewModel = ContactListViewModel(GetContactListUseCase(repository))

        viewModel.uiState.test {
            assertEquals(ContactListUiState.Loading, awaitItem())
            val success = awaitItem()
            assertTrue(success is ContactListUiState.Success)
            assertEquals(1, success.contacts.size)
            assertEquals("John Doe", success.contacts.first().fullName)
        }
    }

    @Test
    fun emits_error_on_failure() = runTest(dispatcher) {
        val repository = FakeRepository(Resource.Error(message = "Boom", code = 500))
        val viewModel = ContactListViewModel(GetContactListUseCase(repository))

        viewModel.uiState.test {
            assertEquals(ContactListUiState.Loading, awaitItem())
            val error = awaitItem()
            assertTrue(error is ContactListUiState.Error)
            assertEquals("Boom", error.message)
        }
    }
}
