package com.emon.mycontactapp.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

/**
 * Aggregates the per-concern Koin modules. Splitting the graph into [networkModule], [dataModule],
 * [domainModule] and [viewModelModule] keeps each layer's wiring isolated and easy to grow — new
 * features add their own module here instead of bloating a single file.
 */
val appModules = listOf(
    networkModule,
    dataModule,
    domainModule,
    viewModelModule
)

/** Called from each platform's entry point (Android Application / iOS init). */
fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        appDeclaration()
        modules(appModules)
    }
}
