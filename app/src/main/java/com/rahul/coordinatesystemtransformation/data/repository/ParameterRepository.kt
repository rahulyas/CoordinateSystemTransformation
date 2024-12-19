package com.rahul.coordinatesystemtransformation.data.repository

import com.rahul.coordinatesystemtransformation.data.model.Parameter
import com.rahul.coordinatesystemtransformation.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ParameterRepository {
    suspend fun insertParameters(parameters: List<Parameter>)
    suspend fun getAllParameters(): Resource<Flow<List<Parameter>>>
    suspend fun getParametersByCode(code: Int): Resource<Flow<List<Parameter>>>
}