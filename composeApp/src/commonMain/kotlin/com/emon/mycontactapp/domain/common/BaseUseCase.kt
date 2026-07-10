package com.emon.mycontactapp.domain.common

import com.emon.mycontactapp.core.utils.Resource
import kotlinx.coroutines.flow.Flow

interface BaseUseCase

interface ApiUseCaseParams<Params, Type> : BaseUseCase {
    suspend fun invoke(params: Params): Flow<Resource<Type>>
}

interface ApiUseCaseNonParams<Type> : BaseUseCase {
    suspend fun invoke(): Flow<Resource<Type>>
}