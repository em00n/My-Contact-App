package com.emon.mycontactapp.di

import com.emon.mycontactapp.data.common.NetworkBoundResource
import com.emon.mycontactapp.data.remote.api.ContactApi
import com.emon.mycontactapp.data.remote.createHttpClient
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Networking graph: Ktor client, API services, and the shared request wrapper.
 * (Koin replacement for the old Hilt `NetworkModule`.)
 */
val networkModule: Module = module {
    single { createHttpClient() }
    single { ContactApi(get()) }
    single { NetworkBoundResource() }
}
