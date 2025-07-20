package com.emon.mycontactapp.data.repository


import com.emon.mycontactapp.core.utils.Resource
import com.emon.mycontactapp.core.utils.mapResponseWith
import com.emon.mycontactapp.data.common.NetworkBoundResource
import com.emon.mycontactapp.data.mapper.ContactListMapper
import com.emon.mycontactapp.data.remote.api.ApiService
import com.emon.mycontactapp.domain.model.ContactList
import com.emon.mycontactapp.domain.repository.ContactListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ContactListRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val contactListMapper: ContactListMapper,
    private val networkBoundResources: NetworkBoundResource,
) : ContactListRepository {

    override suspend fun fetchContactList(): Flow<Resource<ContactList>> {

        return networkBoundResources.performApiRequest {
            apiService.fetchContactList()
        }.mapResponseWith(contactListMapper)
    }
}