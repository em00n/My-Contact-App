package com.emon.mycontactapp.data.mapper

import com.emon.mycontactapp.data.remote.model.ContactListApiResponse
import com.emon.mycontactapp.data.remote.model.ContactListResult
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ContactListMapperTest {
    private lateinit var mapper: ContactListMapper

    @Before
    fun setUp() {
        mapper = ContactListMapper()
    }

    @Test
    fun `mapFromApiResponse with valid data maps correctly`() {
        val apiResponse = ContactListApiResponse(
            result = listOf(
                ContactListResult(
                    email = "test@test.com",
                    full_name = "Test User",
                    image = "http://test.com/image.jpg",
                    phone_number = "1234567890"
                )
            )
        )

        val result = mapper.mapFromApiResponse(apiResponse)

        assertEquals(1, result.result.size)
        with(result.result[0]) {
            assertEquals("test@test.com", email)
            assertEquals("Test User", fullName)
            assertEquals("http://test.com/image.jpg", imageUrl)
            assertEquals("1234567890", phoneNumber)
        }
    }

    @Test
    fun `mapFromApiResponse with null values uses default empty strings`() {
        val apiResponse = ContactListApiResponse(
            result = listOf(
                ContactListResult(
                    email = null,
                    full_name = null,
                    image = null,
                    phone_number = null
                )
            )
        )

        val result = mapper.mapFromApiResponse(apiResponse)

        assertEquals(1, result.result.size)
        with(result.result[0]) {
            assertEquals("", email)
            assertEquals("", fullName)
            assertEquals("", imageUrl)
            assertEquals("", phoneNumber)
        }
    }

    @Test
    fun `mapFromApiResponse with null result list returns empty list`() {
        val apiResponse = ContactListApiResponse(result = null)

        val result = mapper.mapFromApiResponse(apiResponse)

        assertEquals(0, result.result.size)
    }

    @Test
    fun `mapFromApiResponse with multiple contacts maps all correctly`() {
        val apiResponse = ContactListApiResponse(
            result = listOf(
                ContactListResult(
                    email = "test1@test.com",
                    full_name = "Test User 1",
                    image = "http://test.com/image1.jpg",
                    phone_number = "1234567890"
                ),
                ContactListResult(
                    email = "test2@test.com",
                    full_name = "Test User 2",
                    image = "http://test.com/image2.jpg",
                    phone_number = "0987654321"
                )
            )
        )

        val result = mapper.mapFromApiResponse(apiResponse)

        assertEquals(2, result.result.size)
        with(result.result[0]) {
            assertEquals("test1@test.com", email)
            assertEquals("Test User 1", fullName)
            assertEquals("http://test.com/image1.jpg", imageUrl)
            assertEquals("1234567890", phoneNumber)
        }
        with(result.result[1]) {
            assertEquals("test2@test.com", email)
            assertEquals("Test User 2", fullName)
            assertEquals("http://test.com/image2.jpg", imageUrl)
            assertEquals("0987654321", phoneNumber)
        }
    }
}
