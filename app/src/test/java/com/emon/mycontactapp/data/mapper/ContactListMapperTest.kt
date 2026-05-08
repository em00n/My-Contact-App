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
                    id = 1,
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
                    id = null,
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
        // Given
        val apiResponse = ContactListApiResponse(result = null)

        // When
        val result = mapper.mapFromApiResponse(apiResponse)

        // Then
        assertEquals("Expected empty list when result is null", 0, result.result.size)
    }

    @Test
    fun `mapFromApiResponse with multiple contacts maps all correctly`() {
        // Given
        val apiResponse = ContactListApiResponse(
            result = listOf(
                ContactListResult(
                    id = 1,
                    email = "test1@test.com",
                    full_name = "Test User 1",
                    image = "http://test.com/image1.jpg",
                    phone_number = "1234567890"
                ),
                ContactListResult(
                    id = 2,
                    email = "test2@test.com",
                    full_name = "Test User 2",
                    image = "http://test.com/image2.jpg",
                    phone_number = "0987654321"
                )
            )
        )

        // When
        val result = mapper.mapFromApiResponse(apiResponse)

        // Then
        assertEquals("Expected two contacts", 2, result.result.size)
        with(result.result[0]) {
            assertEquals("Expected correct ID for first contact", 1, id)
            assertEquals("Expected correct email for first contact", "test1@test.com", email)
            assertEquals("Expected correct full name for first contact", "Test User 1", fullName)
            assertEquals(
                "Expected correct image URL for first contact",
                "http://test.com/image1.jpg",
                imageUrl
            )
            assertEquals(
                "Expected correct phone number for first contact",
                "1234567890",
                phoneNumber
            )
        }
        with(result.result[1]) {
            assertEquals("Expected correct ID for second contact", 2, id)
            assertEquals("Expected correct email for second contact", "test2@test.com", email)
            assertEquals("Expected correct full name for second contact", "Test User 2", fullName)
            assertEquals(
                "Expected correct image URL for second contact",
                "http://test.com/image2.jpg",
                imageUrl
            )
            assertEquals(
                "Expected correct phone number for second contact",
                "0987654321",
                phoneNumber
            )
        }
    }

    @Test
    fun `mapFromApiResponse with mixed null and valid values handles correctly`() {
        // Given
        val apiResponse = ContactListApiResponse(
            result = listOf(
                ContactListResult(
                    id = 1,
                    email = "test@test.com",
                    full_name = null,
                    image = "http://test.com/image.jpg",
                    phone_number = null
                )
            )
        )

        // When
        val result = mapper.mapFromApiResponse(apiResponse)

        // Then
        assertEquals("Expected one contact", 1, result.result.size)
        with(result.result[0]) {
            assertEquals("Expected correct ID", 1, id)
            assertEquals("Expected correct email", "test@test.com", email)
            assertEquals("Expected empty string for null full name", "", fullName)
            assertEquals("Expected correct image URL", "http://test.com/image.jpg", imageUrl)
            assertEquals("Expected empty string for null phone number", "", phoneNumber)
        }
    }

    @Test
    fun `mapFromApiResponse with empty list returns empty result`() {
        // Given
        val apiResponse = ContactListApiResponse(result = emptyList())

        // When
        val result = mapper.mapFromApiResponse(apiResponse)

        // Then
        assertEquals("Expected empty result for empty list", 0, result.result.size)
    }
}
