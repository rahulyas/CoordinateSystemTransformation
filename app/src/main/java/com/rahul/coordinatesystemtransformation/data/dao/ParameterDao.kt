package com.rahul.coordinatesystemtransformation.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rahul.coordinatesystemtransformation.data.model.Parameter
import com.rahul.coordinatesystemtransformation.utils.Resource
import kotlinx.coroutines.flow.Flow

@Dao
interface ParameterDao {

    @Insert
    suspend fun insertParameters(parameters: List<Parameter>)

    @Query("SELECT * FROM parameters")
    fun getAllParameters(): Flow<List<Parameter>>

    @Query("SELECT * FROM parameters WHERE code = :code")
    fun getParametersByCode(code: Int): Flow<List<Parameter>>
}
