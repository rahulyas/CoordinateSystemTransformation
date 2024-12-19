package com.rahul.coordinatesystemtransformation.di

import com.rahul.coordinatesystemtransformation.data.repository.ParameterRepository
import com.rahul.coordinatesystemtransformation.data.repository.ParameterRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
fun interface RepositoryModule {
    @Binds
    fun bindParameterRepository(impl: ParameterRepositoryImpl): ParameterRepository
}