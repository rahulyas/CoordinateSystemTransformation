package com.rahul.coordinatesystemtransformation.data.repository

import android.util.Log
import com.rahul.coordinatesystemtransformation.data.dao.ParameterDao
import com.rahul.coordinatesystemtransformation.data.model.Parameter
import com.rahul.coordinatesystemtransformation.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ParameterRepositoryImpl @Inject constructor(
    private val parameterDao: ParameterDao
) : ParameterRepository  {
    companion object{
        const val TAG = "ParameterRepository"
    }
    override suspend fun insertParameters(parameters: List<Parameter>) {
        try {
            parameterDao.insertParameters(parameters)
        }catch (e:Exception){
            Log.e(TAG, "insertParameters:-${e.message} ")
        }
    }

    override suspend fun getAllParameters(): Resource<Flow<List<Parameter>>> {
        val result = parameterDao.getAllParameters()
        return Resource.Success(result)
    }

    override suspend fun getParametersByCode(code: Int): Resource<Flow<List<Parameter>>> {
        val result = parameterDao.getParametersByCode(code)
        return Resource.Success(result)
    }

}