package com.rahul.coordinatesystemtransformation.utils

import android.content.Context
import android.content.SharedPreferences

class PreferencesHelper(private val context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

    companion object {
        const val IS_CSV_LOADED = "is_csv_loaded"
    }

    fun isCsvLoaded(): Boolean {
        return sharedPreferences.getBoolean(IS_CSV_LOADED, false)
    }

    fun setCsvLoaded(isLoaded: Boolean) {
        sharedPreferences.edit().putBoolean(IS_CSV_LOADED, isLoaded).apply()
    }
}
