package com.emon.mycontactapp

import app.cash.turbine.test
import com.emon.mycontactapp.core.utils.Resource
import com.emon.mycontactapp.data.common.NetworkBoundResource
import com.emon.mycontactapp.data.mapper.ContactListMapper
import com.emon.mycontactapp.data.remote.api.ApiService
import com.emon.mycontactapp.data.remote.model.ContactListApiResponse
import com.emon.mycontactapp.data.repository.ContactListRepositoryImpl
import com.emon.mycontactapp.domain.model.ContactList
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ContactListRepositoryImplTest {
    private lateinit var apiService: ApiService
    private lateinit var contactListMapper: ContactListMapper
    private lateinit var networkBoundResource: NetworkBoundResource
    private lateinit var repository: ContactListRepositoryImpl

    @Before
    fun setUp() {
        apiService = mockk()
        contactListMapper = mockk()
        networkBoundResource = mockk()
        repository = ContactListRepositoryImpl(apiService, contactListMapper, networkBoundResource)
    }

    @Test
    fun `fetchContactList emits Success when API and mapping succeed`() = runTest {
        val apiResponse = mockk<ContactListApiResponse>()
        val mapped = ContactList(result = emptyList())
        coEvery { networkBoundResource.performApiRequest<ContactListApiResponse>(any()) } returns flow {
            emit(Resource.Success(apiResponse))
        }
        coEvery { contactListMapper.mapFromApiResponse(apiResponse) } returns mapped

        repository.fetchContactList().test {
            val item = awaitItem()
            assertTrue(item is Resource.Success)
            assertEquals(mapped, (item as Resource.Success).data)
            awaitComplete()
        }
    }

    @Test
    fun `fetchContactList emits Error when API fails`() = runTest {
        coEvery { networkBoundResource.performApiRequest<ContactListApiResponse>(any()) } returns flow {
            emit(Resource.Error("error", 500))
        }
        // Mapper should not be called in error case

        repository.fetchContactList().test {
            val item = awaitItem()
            assertTrue(item is Resource.Error)
            assertEquals("error", (item as Resource.Error).message)
            awaitComplete()
        }
    }

    @Test
    fun `fetchContactList emits Loading when API is loading`() = runTest {
        coEvery { networkBoundResource.performApiRequest<ContactListApiResponse>(any()) } returns flow {
            emit(Resource.Loading(true))
        }
        // Mapper should not be called in loading case

        repository.fetchContactList().test {
            val item = awaitItem()
            assertTrue(item is Resource.Loading)
            assertTrue((item as Resource.Loading).loading)
            awaitComplete()
        }
    }
}
