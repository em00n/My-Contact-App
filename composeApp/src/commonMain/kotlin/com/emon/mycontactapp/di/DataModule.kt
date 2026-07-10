package com.emon.mycontactapp.di

import com.emon.mycontactapp.data.mapper.ContactListMapper
import com.emon.mycontactapp.data.repository.ContactListRepositoryImpl
import com.emon.mycontactapp.domain.repository.ContactListRepository
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Data graph: mappers and repository implementations bound to their domain interfaces.
 * (Koin replacement for the old Hilt `RepositoryModule`.)
 */
val dataModule: Module = module {
    single { ContactListMapper() }
    single<ContactListRepository> { ContactListRepositoryImpl(get(), get(), get()) }
}
