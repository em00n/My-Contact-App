package com.emon.mycontactapp.data.repository


import com.emon.mycontactapp.core.utils.Resource
import com.emon.mycontactapp.core.utils.mapResponseWith
import com.emon.mycontactapp.data.common.NetworkBoundResource
import com.emon.mycontactapp.data.mapper.ContactListMapper
import com.emon.mycontactapp.data.remote.api.ContactApi
import com.emon.mycontactapp.domain.model.ContactList
import com.emon.mycontactapp.domain.repository.ContactListRepository
import kotlinx.coroutines.flow.Flow

class ContactListRepositoryImpl(
    private val contactApi: ContactApi,
    private val contactListMapper: ContactListMapper,
    private val networkBoundResources: NetworkBoundResource,
) : ContactListRepository {

    override suspend fun fetchContactList(): Flow<Resource<ContactList>> {

        return networkBoundResources.performApiRequest {
            contactApi.fetchContactList()
        }.mapResponseWith(contactListMapper)
    }
}