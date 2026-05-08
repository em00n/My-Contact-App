package com.emon.mycontactapp.data.common

import app.cash.turbine.test
import com.emon.mycontactapp.MainDispatcherRule
import com.emon.mycontactapp.TestConstants
import com.emon.mycontactapp.core.utils.Resource
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import kotlin.time.Duration.Companion.seconds

/**
 * Unit tests for [NetworkBoundResource]
 *
 * This test suite verifies the behavior of NetworkBoundResource, which handles API requests
 * and wraps responses in a Flow of Resource states.
 *
 * Test Coverage:
 * 1. Success Scenarios
 *    - Successful API response with data
 *    - Loading state transitions (true -> false)
 *
 * 2. Error Scenarios
 *    - HTTP error responses (404, 500, 401, 403, 502)
 *    - Network exceptions (IOException, SocketTimeoutException)
 *    - Empty/null response handling
 *
 * Each test verifies:
 * - Correct state emission order (Loading -> Result)
 * - Proper error messages and codes
 * - Proper data mapping in success cases
 * - Loading state transitions
 */
@OptIn(ExperimentalCoroutinesApi::class)
class NetworkBoundResourceTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var networkBoundResource: NetworkBoundResource

    @Before
    fun setUp() {
        networkBoundResource = NetworkBoundResource()
    }

    /**
     * Tests successful API response scenario.
     *
     * Verifies:
     * 1. Initial loading state is emitted
     * 2. Success state contains correct data
     */
    @Test
    fun `performApiRequest emits loading and success states for successful response`() = runTest {
        // Given
        val mockData = TestConstants.TEST_DATA
        val mockResponse = Response.success(mockData)
        suspend fun apiCall(): Response<String> = mockResponse

        // When & Then
        networkBoundResource.performApiRequest(::apiCall).test(timeout = TestConstants.TIMEOUT_DURATION.seconds) {
            // First emission should be Loading
            val loading = awaitItem()
            assertTrue("Expected Resource.Loading", loading is Resource.Loading)

            // Second emission should be Success with data
            val success = awaitItem()
            assertTrue("Expected Resource.Success", success is Resource.Success)
            assertEquals("Expected success data", mockData, (success as Resource.Success).data)

            awaitComplete()
        }
    }

    /**
     * Tests error handling for various HTTP error codes.
     *
     * Verifies error handling for:
     * - 404 (Not Found)
     * - 500 (Server Error)
     * - 401 (Unauthorized)
     * - 403 (Forbidden)
     * - 502 (Bad Gateway)
     *
     * Each error code test verifies:
     * 1. Error state is emitted with correct code
     * 2. Loading states are properly managed
     */
    @Test
    fun `performApiRequest emits error for HTTP error responses`() = runTest {
        val errorCodes = listOf(
            TestConstants.HTTP_NOT_FOUND,
            TestConstants.HTTP_SERVER_ERROR,
            TestConstants.HTTP_UNAUTHORIZED,
            TestConstants.HTTP_FORBIDDEN,
            TestConstants.HTTP_BAD_GATEWAY
        )

        errorCodes.forEach { errorCode ->
            // Given
            val errorBody = mockk<ResponseBody> {
                every { contentType() } returns null
                every { contentLength() } returns 0L
                every { string() } returns TestConstants.ERROR_MESSAGE
            }
            val mockResponse: Response<String> = Response.error(errorCode, errorBody)
            suspend fun apiCall(): Response<String> = mockResponse

            // When & Then
            networkBoundResource.performApiRequest(::apiCall).test(timeout = TestConstants.TIMEOUT_DURATION.seconds) {
                // First emission: Loading
                val loading = awaitItem()
                assertTrue("Expected Resource.Loading for error code $errorCode", loading is Resource.Loading)

                // Second emission: Error with correct code
                val error = awaitItem()
                assertTrue("Expected Resource.Error for error code $errorCode", error is Resource.Error)
                assertEquals("Expected error code $errorCode", errorCode, (error as Resource.Error).code)
                awaitComplete()
            }
        }
    }

    /**
     * Tests handling of null response body.
     *
     * Verifies:
     * 1. Error state is emitted with "Unknown error" message
     * 2. Error code is set to default (0)
     * 3. Loading state is properly managed
     */
    @Test
    fun `performApiRequest emits error for null response body`() = runTest {
        // Given
        val mockResponse: Response<String> = Response.success(null)
        suspend fun apiCall(): Response<String> = mockResponse

        // When & Then
        networkBoundResource.performApiRequest(::apiCall).test(timeout = TestConstants.TIMEOUT_DURATION.seconds) {
            // First emission: Loading
            val loading = awaitItem()
            assertTrue("Expected Resource.Loading", loading is Resource.Loading)

            // Second emission: Error with unknown message
            val error = awaitItem()
            assertTrue("Expected Resource.Error", error is Resource.Error)
            assertEquals("Expected unknown error message", TestConstants.UNKNOWN_ERROR, (error as Resource.Error).message)
            assertEquals("Expected default error code", TestConstants.DEFAULT_ERROR_CODE, error.code)
            awaitComplete()
        }
    }

    /**
     * Tests handling of network timeout.
     *
     * Verifies:
     * 1. Error state contains timeout-specific message
     * 2. Loading state is properly emitted
     * 3. Exception is properly caught and transformed
     */
    @Test
    fun `performApiRequest handles SocketTimeoutException`() = runTest {
        // Given
        suspend fun apiCall(): Response<String> = throw SocketTimeoutException()

        // When & Then
        networkBoundResource.performApiRequest(::apiCall).test(timeout = TestConstants.TIMEOUT_DURATION.seconds) {
            // First emission: Loading
            val loading = awaitItem()
            assertTrue("Expected Resource.Loading", loading is Resource.Loading)

            // Second emission: Error with timeout message
            val error = awaitItem()
            assertTrue("Expected Resource.Error", error is Resource.Error)
            assertEquals("Expected timeout error message", TestConstants.TIMEOUT_ERROR, (error as Resource.Error).message)
            awaitComplete()
        }
    }

    /**
     * Tests handling of network/IO errors.
     *
     * Verifies:
     * 1. Error state contains internet connection message
     * 2. Loading state is properly emitted
     * 3. Exception is properly caught and transformed
     */
    @Test
    fun `performApiRequest handles IOException`() = runTest {
        // Given
        suspend fun apiCall(): Response<String> = throw IOException()

        // When & Then
        networkBoundResource.performApiRequest(::apiCall).test(timeout = TestConstants.TIMEOUT_DURATION.seconds) {
            // First emission: Loading
            val loading = awaitItem()
            assertTrue("Expected Resource.Loading", loading is Resource.Loading)

            // Second emission: Error with internet connection message
            val error = awaitItem()
            assertTrue("Expected Resource.Error", error is Resource.Error)
            assertEquals("Expected internet error message", TestConstants.INTERNET_ERROR, (error as Resource.Error).message)
            awaitComplete()
        }
    }
}
