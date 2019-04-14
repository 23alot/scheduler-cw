package com.boscatov.schedulercw.data.repository.preferences

import android.content.Context
import javax.inject.Inject

/**
 * Created by boscatov on 14.04.2019.
 */

class NNSharedPreferencesImpl @Inject constructor(
    private val context: Context
): NNSharedPreferences {
    companion object {
        private val NAME = "NN_VALUES"
        private val VALUES = "VALUES"
    }
    private val preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
    override fun getValues(): List<Double> {
        val values = preferences.getString(VALUES, "430,560")?.split(",")?.map { it.toDouble() } ?: listOf()
        return values
    }

    override fun setValues(values: List<Double>) {
        val result = values.joinToString(",")
        preferences.edit().apply {
            putString(VALUES, result)
        }.apply()
    }
}