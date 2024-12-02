package com.emon.mycontactapp.data.remote

import android.content.Context
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.ResponseBody.Companion.toResponseBody

class MockResponseInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val request = chain.request()

        return try {
            // Mock only the specific endpoint
            if (request.url.encodedPath == "/api/user_journey/contact_list") {
                // Load mock data from assets
                val response = getJsonFromAssets("mock_contact_list.json", context)

                // Return a mock response
                okhttp3.Response.Builder()
                    .request(request)
                    .protocol(Protocol.HTTP_1_1)
                    .code(200) // Success code
                    .message("OK")
                    .body(response.toResponseBody("application/json".toMediaTypeOrNull()))
                    .build()
            } else {
                // Proceed with the actual network request for other endpoints
                chain.proceed(request)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Return a 500 error for debugging
            okhttp3.Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(500) // Internal Server Error
                .message("Mock Interceptor Error: ${e.message}")
                .body("{}".toResponseBody("application/json".toMediaTypeOrNull()))
                .build()
        }
    }

    // Helper function to load JSON from assets
    private fun getJsonFromAssets(fileName: String, context: Context): String {
        return try {
            context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (e: Exception) {
            throw RuntimeException("Error loading mock JSON: $fileName", e)
        }
    }
}
