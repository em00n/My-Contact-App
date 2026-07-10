package com.emon.mycontactapp.data.mapper

import com.emon.mycontactapp.core.utils.Mapper
import com.emon.mycontactapp.data.remote.model.ContactListApiResponse
import com.emon.mycontactapp.domain.model.Contact
import com.emon.mycontactapp.domain.model.ContactList

class ContactListMapper : Mapper<ContactListApiResponse, ContactList> {
    override fun mapFromApiResponse(type: ContactListApiResponse): ContactList {
        return ContactList(
            result = type.result?.map {
                Contact(
                    id = it.id ?: 0,
                    email = it.email ?: "",
                    fullName = it.fullName ?: "",
                    imageUrl = it.image ?: "",
                    phoneNumber = it.phoneNumber ?: ""
                )
            } ?: emptyList()
        )
    }
}