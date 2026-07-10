package com.emon.mycontactapp.domain.repository

import com.emon.mycontactapp.core.utils.Resource
import com.emon.mycontactapp.domain.model.ContactList
import kotlinx.coroutines.flow.Flow


interface ContactListRepository {

    suspend fun fetchContactList(): Flow<Resource<ContactList>>
}