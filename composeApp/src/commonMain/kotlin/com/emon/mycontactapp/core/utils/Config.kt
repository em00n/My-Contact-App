package com.emon.mycontactapp.core.utils

/**
 * Replaces the old Android `BuildConfig.BASE_URL`. In a real multiplatform app this would be
 * generated per build type (e.g. via BuildKonfig) — kept as a constant here because all data is
 * served by the in-app Ktor MockEngine.
 */
object Config {
    const val BASE_URL = "https://dev.localhost.com/"
    const val CONTACT_LIST_PATH = "api/user_journey/contact_list"
}
