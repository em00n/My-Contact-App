package com.emon.mycontactapp.data.remote.api

import com.emon.mycontactapp.data.remote.model.ContactListApiResponse
import retrofit2.Response
import retrofit2.http.GET


interface ApiService {

    @GET("api/user_journey/contact_list")
    suspend fun fetchContactList(): Response<ContactListApiResponse>
}