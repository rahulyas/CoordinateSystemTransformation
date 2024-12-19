package com.rahul.coordinatesystemtransformation.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "parameters")
data class Parameter(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val code: String,
    val type: String,
    val extent: String,
    val dataSource: String,
    val remarks: String?,
    val revisionDate: String
)
