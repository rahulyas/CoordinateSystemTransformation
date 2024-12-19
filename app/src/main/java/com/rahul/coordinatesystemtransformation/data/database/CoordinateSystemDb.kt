package com.rahul.coordinatesystemtransformation.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rahul.coordinatesystemtransformation.data.dao.ParameterDao
import com.rahul.coordinatesystemtransformation.data.model.Parameter


@Database(entities = [Parameter::class], version = 2, exportSchema = false)
abstract class CoordinateSystemDb : RoomDatabase() {
    abstract fun parameterDao(): ParameterDao
}