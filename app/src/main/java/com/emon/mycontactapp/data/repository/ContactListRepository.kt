package com.emon.mycontactapp.data.repository

import com.emon.mycontactapp.model.ContactListApiResponse
import kotlinx.coroutines.flow.Flow
import com.emon.mycontactapp.utils.Result


interface ContactListRepository {

    suspend fun fetchContactList(): Flow<Result<ContactListApiResponse>>
}