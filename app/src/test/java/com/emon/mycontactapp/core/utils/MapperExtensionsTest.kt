package com.emon.mycontactapp.core.utils

import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class MapperExtensionsTest {

    private val testMapper = object : Mapper<String, Int> {
        override fun mapFromApiResponse(type: String): Int = type.length
    }

    @Test
    fun `mapResponseWith maps Resource_Success correctly`() = runTest {
        // Given
        val flow = flowOf<Resource<String>>(Resource.Success("hello"))

        // When & Then
        flow.mapResponseWith(testMapper).test(timeout = 2.seconds) {
            val item = awaitItem()
            assertTrue("Expected Resource.Success", item is Resource.Success)
            assertEquals(5, (item as Resource.Success).data)
            awaitComplete()
        }
    }

    @Test
    fun `mapResponseWith passes through Resource_Error`() = runTest {
        // Given
        val errorMessage = "error"
        val errorCode = 404
        val flow = flowOf<Resource<String>>(Resource.Error(errorMessage, errorCode))

        // When & Then
        flow.mapResponseWith(testMapper).test(timeout = 2.seconds) {
            val item = awaitItem()
            assertTrue("Expected Resource.Error", item is Resource.Error)
            val errorItem = item as Resource.Error
            assertEquals(errorMessage, errorItem.message)
            assertEquals(errorCode, errorItem.code)
            awaitComplete()
        }
    }

    @Test
    fun `mapResponseWith passes through Resource_Loading`() = runTest {
        // Given
        val flow = flowOf<Resource<String>>(Resource.Loading)

        // When & Then
        flow.mapResponseWith(testMapper).test(timeout = 2.seconds) {
            val item = awaitItem()
            assertTrue("Expected Resource.Loading", item is Resource.Loading)
            awaitComplete()
        }
    }

    @Test
    fun `mapResponseWith handles multiple emissions correctly`() = runTest {
        // Given
        val flow = flowOf<Resource<String>>(
            Resource.Loading,
            Resource.Success("hello"),
            Resource.Loading
        )

        // When & Then
        flow.mapResponseWith(testMapper).test(timeout = 2.seconds) {
            // First emission: Loading
            val loading = awaitItem()
            assertTrue("Expected first Resource.Loading", loading is Resource.Loading)

            // Second emission: Success with mapped data
            val success = awaitItem()
            assertTrue("Expected Resource.Success", success is Resource.Success)
            assertEquals("Expected mapped data length to be 5", 5, (success as Resource.Success).data)

            // Third emission: Loading
            val loadingComplete = awaitItem()
            assertTrue("Expected second Resource.Loading", loadingComplete is Resource.Loading)

            awaitComplete()
        }
    }

    @Test
    fun `mapResponseWith maps Success with different data type`() = runTest {
        // Given - mapper that converts String to character count
        val customMapper = object : Mapper<String, Int> {
            override fun mapFromApiResponse(type: String): Int = type.length * 2
        }
        val flow = flowOf<Resource<String>>(Resource.Success("test"))

        // When & Then
        flow.mapResponseWith(customMapper).test(timeout = 2.seconds) {
            val item = awaitItem()
            assertTrue("Expected Resource.Success", item is Resource.Success)
            assertEquals("Expected mapped data to be 8 (4*2)", 8, (item as Resource.Success).data)
            awaitComplete()
        }
    }

    @Test
    fun `mapResponseWith preserves Error details through mapping`() = runTest {
        // Given
        val errorMessage = "Network timeout"
        val errorCode = 503
        val flow = flowOf<Resource<String>>(Resource.Error(errorMessage, errorCode))

        // When & Then
        flow.mapResponseWith(testMapper).test(timeout = 2.seconds) {
            val item = awaitItem()
            val errorItem = item as? Resource.Error
            assertNotNull("Expected Resource.Error", errorItem)
            assertEquals(errorMessage, errorItem?.message)
            assertEquals(errorCode, errorItem?.code)
            awaitComplete()
        }
    }
}