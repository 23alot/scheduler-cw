package com.boscatov.schedulercw.view.ui.fragment.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.boscatov.schedulercw.R
import com.boscatov.schedulercw.view.viewmodel.holder.MainViewModel
import com.boscatov.schedulercw.view.viewmodel.stats.StatsViewModel
import kotlinx.android.synthetic.main.fragment_stats.*
import java.text.FieldPosition
import java.text.Format
import java.text.ParsePosition
import android.graphics.Color
import com.androidplot.Plot
import com.androidplot.Series
import com.androidplot.ui.Formatter
import com.androidplot.ui.SeriesRenderer
import com.androidplot.xy.*


class StatsFragment : Fragment() {

    lateinit var mainViewModel: MainViewModel
    lateinit var statsViewModel: StatsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_stats, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = activity?.run {
            ViewModelProviders.of(this).get(MainViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        statsViewModel = ViewModelProviders.of(this).get(StatsViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val labels = listOf<Number>(1, 2, 3, 6, 7, 8, 9, 10, 13, 14)
        val series1 = listOf<Number>(1, 4, 2, 8, 4, 16, 8, 32, 16, 64)
        val series2 = listOf<Number>(5, 2, 10, 5, 20, 10, 40, 20, 80, 40)
        val ser1 = SimpleXYSeries(
            series1, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series1"
        )
        val ser2 = SimpleXYSeries(
            series2, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series2"
        )

        val series1Format = MyFormatter(Color.RED, Color.BLUE)

        val series2Format = MyFormatter(Color.BLUE, Color.RED)

        fragmentStatsPlot.addSeries(ser1, series1Format)
        fragmentStatsPlot.addSeries(ser2, series2Format)
        fragmentStatsPlot.rangeStepModel = StepModel(StepMode.INCREMENT_BY_VAL, 5.0)
        fragmentStatsPlot.setRangeLowerBoundary(0, BoundaryMode.FIXED)

        fragmentStatsPlot.graph.getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).format = object : Format() {
            override fun format(obj: Any, toAppendTo: StringBuffer, pos: FieldPosition): StringBuffer {
                val i = Math.round((obj as Number).toFloat())
                return toAppendTo.append(labels[i])
            }

            override fun parseObject(source: String, pos: ParsePosition): Any? {
                return null
            }
        }

        val renderer = fragmentStatsPlot.getRenderer(
            MyRenderer::class.java)

        renderer.barOrientation = BarRenderer.BarOrientation.STACKED
    }

    private class MyRenderer(plot: XYPlot): BarRenderer<MyFormatter>(plot)

    private class MyFormatter(a: Int, b: Int): BarFormatter(a, b) {
        override fun getRendererClass(): Class<out SeriesRenderer<*,*,*>> {
            return MyRenderer::class.java
        }

        override fun doGetRendererInstance(plot: XYPlot): SeriesRenderer<out Plot<*, *, *, *, *>, *, out Formatter<*>> {
            return MyRenderer(plot)
        }
    }
}