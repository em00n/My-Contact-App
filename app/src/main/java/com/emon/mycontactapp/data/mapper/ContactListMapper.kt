package com.emon.mycontactapp.data.mapper

import com.emon.mycontactapp.core.utils.Mapper
import com.emon.mycontactapp.data.remote.model.ContactListApiResponse
import com.emon.mycontactapp.domain.model.Contact
import com.emon.mycontactapp.domain.model.ContactList
import javax.inject.Inject

class ContactListMapper @Inject constructor() : Mapper<ContactListApiResponse, ContactList> {
    override fun mapFromApiResponse(type: ContactListApiResponse): ContactList {
        return ContactList(
            result = type.result?.map {
                Contact(
                    id = it.id ?: 0,
                    email = it.email ?: "",
                    fullName = it.full_name ?: "",
                    imageUrl = it.image ?: "",
                    phoneNumber = it.phone_number ?: ""
                )
            } ?: emptyList()
        )
    }
}