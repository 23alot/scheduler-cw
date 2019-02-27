package com.boscatov.chart.entity

/**
 * Created by boscatov on 26.02.2019.
 */
interface ChartValue {
    fun getDisplayValue(): String
    fun getPositionXValue(): Float
    fun getPositionYValue(): Float
}