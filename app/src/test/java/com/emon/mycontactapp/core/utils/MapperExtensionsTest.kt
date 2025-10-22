package com.emon.mycontactapp.core.utils

import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
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
            assertTrue(item is Resource.Success)
            assertEquals(5, (item as Resource.Success).data)
            awaitComplete()
        }
    }

    @Test
    fun `mapResponseWith passes through Resource_Error`() = runTest {
        // Given
        val flow = flowOf<Resource<String>>(Resource.Error("error", 404))

        // When & Then
        flow.mapResponseWith(testMapper).test(timeout = 2.seconds) {
            val item = awaitItem()
            assertTrue(item is Resource.Error)
            assertEquals("error", (item as Resource.Error).message)
            assertEquals(404, (item as Resource.Error).code)
            awaitComplete()
        }
    }

    @Test
    fun `mapResponseWith passes through Resource_Loading`() = runTest {
        // Given
        val flow = flowOf<Resource<String>>(Resource.Loading(true))

        // When & Then
        flow.mapResponseWith(testMapper).test(timeout = 2.seconds) {
            val item = awaitItem()
            assertTrue(item is Resource.Loading)
            assertTrue((item as Resource.Loading).loading)
            awaitComplete()
        }
    }

    @Test
    fun `mapResponseWith handles multiple emissions correctly`() = runTest {
        // Given
        val flow = flowOf<Resource<String>>(
            Resource.Loading(true),
            Resource.Success("hello"),
            Resource.Loading(false)
        )

        // When & Then
        flow.mapResponseWith(testMapper).test(timeout = 2.seconds) {
            // First emission: Loading
            val loading = awaitItem()
            assertTrue(loading is Resource.Loading)
            assertTrue((loading as Resource.Loading).loading)

            // Second emission: Success with mapped data
            val success = awaitItem()
            assertTrue(success is Resource.Success)
            assertEquals(5, (success as Resource.Success).data)

            // Third emission: Loading false
            val loadingComplete = awaitItem()
            assertTrue(loadingComplete is Resource.Loading)
            assertTrue(!(loadingComplete as Resource.Loading).loading)

            awaitComplete()
        }
    }
}