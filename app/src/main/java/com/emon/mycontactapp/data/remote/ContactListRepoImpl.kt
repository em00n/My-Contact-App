package com.emon.mycontactapp.data.remote


import com.emon.mycontactapp.data.repository.ContactListRepository
import com.emon.mycontactapp.data.wrapper.NetworkBoundResource
import com.emon.mycontactapp.model.ContactListApiResponse
import com.emon.mycontactapp.utils.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ContactListRepoImpl @Inject constructor(
    private val apiService: ApiService,
    private val networkBoundResources: NetworkBoundResource,
) : ContactListRepository {

    override suspend fun fetchContactList(): Flow<Result<ContactListApiResponse>> {

        return networkBoundResources.downloadData {
            apiService.fetchContactList()
        }
    }
}