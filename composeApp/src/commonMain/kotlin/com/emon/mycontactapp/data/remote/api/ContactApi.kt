package com.emon.mycontactapp.data.remote.api

import com.emon.mycontactapp.core.utils.Config
import com.emon.mycontactapp.data.remote.model.ContactListApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

/**
 * Ktor replacement for the old Retrofit `ApiService`.
 */
class ContactApi(private val client: HttpClient) {

    suspend fun fetchContactList(): ContactListApiResponse =
        client.get(Config.CONTACT_LIST_PATH).body()
}
