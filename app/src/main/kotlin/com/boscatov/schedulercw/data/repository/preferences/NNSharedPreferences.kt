package com.boscatov.schedulercw.data.repository.preferences

/**
 * Created by boscatov on 14.04.2019.
 */

interface NNSharedPreferences {
    fun getValues(): List<Double>
    fun setValues(values: List<Double>)
}