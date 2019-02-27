package com.boscatov.chart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.boscatov.chart.entity.ChartValue

class LineChartView: View {
    private val values = mutableListOf<ChartValue>()
    private val paint = Paint()
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val width = width
        val height = height
        for (i in 0 until values.size - 1) {
            val startX = values[i].getPositionXValue() * width
            val startY = values[i].getPositionYValue() * height
            val endX = values[i+1].getPositionXValue() * width
            val endY = values[i+1].getPositionYValue() * height
            canvas.drawLine(startX, startY, endX, endY, paint)
        }
    }
}