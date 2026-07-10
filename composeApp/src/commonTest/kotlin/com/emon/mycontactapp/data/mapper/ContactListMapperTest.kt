package com.emon.mycontactapp.data.mapper

import com.emon.mycontactapp.data.remote.model.ContactListApiResponse
import com.emon.mycontactapp.data.remote.model.ContactListResult
import kotlin.test.Test
import kotlin.test.assertEquals

class ContactListMapperTest {

    private val mapper = ContactListMapper()

    @Test
    fun maps_fields_and_defaults_nulls() {
        val response = ContactListApiResponse(
            result = listOf(
                ContactListResult(
                    id = 1,
                    email = "john@example.com",
                    fullName = "John Doe",
                    image = "https://img/1",
                    phoneNumber = "+123"
                ),
                ContactListResult(
                    id = null,
                    email = null,
                    fullName = null,
                    image = null,
                    phoneNumber = null
                )
            )
        )

        val result = mapper.mapFromApiResponse(response)

        assertEquals(2, result.result.size)
        val first = result.result[0]
        assertEquals(1, first.id)
        assertEquals("John Doe", first.fullName)
        assertEquals("https://img/1", first.imageUrl)

        val second = result.result[1]
        assertEquals(0, second.id)
        assertEquals("", second.fullName)
        assertEquals("", second.imageUrl)
    }

    @Test
    fun null_result_maps_to_empty_list() {
        val result = mapper.mapFromApiResponse(ContactListApiResponse(result = null))
        assertEquals(0, result.result.size)
    }
}
