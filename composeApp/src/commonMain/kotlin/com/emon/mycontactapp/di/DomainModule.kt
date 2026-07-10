package com.emon.mycontactapp.di

import com.emon.mycontactapp.domain.usecase.GetContactListUseCase
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Domain graph: use cases. Declared as `factory` so each consumer gets a fresh, stateless instance.
 */
val domainModule: Module = module {
    factory { GetContactListUseCase(get()) }
}
