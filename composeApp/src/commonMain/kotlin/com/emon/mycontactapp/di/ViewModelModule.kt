package com.emon.mycontactapp.di

import com.emon.mycontactapp.presentation.contactlist.ContactListViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Presentation graph: ViewModels. Uses Koin's multiplatform `viewModel { }` so the same
 * declaration works for both Android and iOS.
 */
val viewModelModule: Module = module {
    viewModel { ContactListViewModel(get()) }
}
