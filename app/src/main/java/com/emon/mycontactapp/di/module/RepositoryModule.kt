package com.emon.mycontactapp.di.module

import com.emon.mycontactapp.data.remote.ContactListRepoImpl
import com.emon.mycontactapp.data.repository.ContactListRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindContactListRepository(contactListRepoImpl: ContactListRepoImpl): ContactListRepository
}