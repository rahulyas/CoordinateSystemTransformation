package com.rahul.coordinatesystemtransformation.di

import android.content.Context
import androidx.room.Room
import com.rahul.coordinatesystemtransformation.data.dao.ParameterDao
import com.rahul.coordinatesystemtransformation.data.database.CoordinateSystemDb
import com.rahul.coordinatesystemtransformation.data.repository.ParameterRepository
import com.rahul.coordinatesystemtransformation.data.usecase.GetAllParametersUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext app: Context): CoordinateSystemDb {
        return Room.databaseBuilder(app, CoordinateSystemDb::class.java, "coordinateSystem_database").build()
    }

    @Provides
    fun provideParameterDao(db: CoordinateSystemDb): ParameterDao = db.parameterDao()

    @Provides
    fun provideGetAllParametersUseCase(parameterRepository: ParameterRepository): GetAllParametersUseCase {
        return GetAllParametersUseCase(parameterRepository) // Adjust the construction as per your logic
    }
}