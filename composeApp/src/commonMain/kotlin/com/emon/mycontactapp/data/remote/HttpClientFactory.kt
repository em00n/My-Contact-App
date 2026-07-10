package com.emon.mycontactapp.data.remote

import com.emon.mycontactapp.core.utils.Config
import com.emon.mycontactapp.resources.Res
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.url
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.ExperimentalResourceApi

/**
 * Builds the Ktor [HttpClient]. This replaces the old OkHttp + Retrofit + MockResponseInterceptor
 * stack. The mock JSON that used to live in Android `assets/` now lives in `composeResources/files`
 * and is served through Ktor's [MockEngine], so the whole data layer stays in commonMain with no
 * platform `Context` dependency.
 */
@OptIn(ExperimentalResourceApi::class)
fun createHttpClient(): HttpClient {
    val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    return HttpClient(MockEngine) {
        install(ContentNegotiation) {
            json(json)
        }
        install(Logging) {
            level = LogLevel.INFO
        }
        defaultRequest {
            url(Config.BASE_URL)
        }
        engine {
            addHandler { request ->
                when (request.url.encodedPath) {
                    "/${Config.CONTACT_LIST_PATH}" -> {
                        val body = Res.readBytes("files/mock_contact_list.json").decodeToString()
                        respond(
                            content = body,
                            status = HttpStatusCode.OK,
                            headers = headersOf(HttpHeaders.ContentType, "application/json")
                        )
                    }

                    else -> respond(
                        content = "{}",
                        status = HttpStatusCode.NotFound,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }
            }
        }
    }
}
