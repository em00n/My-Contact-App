package com.emon.mycontactapp.domain.usecase

import com.emon.mycontactapp.domain.common.ApiUseCaseNonParams
import com.emon.mycontactapp.domain.model.ContactList
import com.emon.mycontactapp.domain.repository.ContactListRepository
import com.emon.mycontactapp.core.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetContactListUseCase @Inject constructor(
    private val repository: ContactListRepository
) : ApiUseCaseNonParams<ContactList> {
    override suspend fun invoke(): Flow<Resource<ContactList>> {
        return repository.fetchContactList()
    }
}