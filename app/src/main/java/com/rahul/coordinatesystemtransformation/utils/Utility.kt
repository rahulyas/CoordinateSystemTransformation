package com.rahul.coordinatesystemtransformation.utils

import android.content.Context
import com.rahul.coordinatesystemtransformation.data.model.Parameter
import java.io.BufferedReader
import java.io.InputStreamReader

fun readCsvFile(context: Context, resourceId: Int): List<Parameter> {
    val inputStream = context.resources.openRawResource(resourceId)
    val reader = BufferedReader(InputStreamReader(inputStream))
    val parameters = mutableListOf<Parameter>()
    reader.useLines { lines ->
        lines.drop(1).forEach { line -> // Skip header
            val tokens = line.split(",")
            parameters.add(
                Parameter(
                    name = tokens[0],
                    code = tokens[1],
                    type = tokens[2],
                    extent = tokens[3],
                    dataSource = tokens[4],
                    remarks = tokens[5].takeIf { it.isNotEmpty() },
                    revisionDate = tokens[6]
                )
            )
        }
    }
    return parameters
}
