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
import com.boscatov.schedulercw.data.entity.Task
import com.boscatov.schedulercw.data.entity.TaskStatus
import java.util.*
import java.util.Collections.max


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
        statsViewModel.tasks.observe(this, androidx.lifecycle.Observer {
            showGraph(it)
            fragmentStatsTimeSpentTV.setText(timeSpent(it))
            fragmentStatsTasksDonePB.progress = tasksDone(it)
        })
        statsViewModel.loadTasks()
    }

    private fun timeSpent(tasks: List<Task>): String {
        val result = tasks.sumBy { (it.taskDuration - (it.taskSpent?:0)).toInt() }

        return if (result > 0) {
            "You've spent $result less than planned"
        } else if (result == 0){
            "You've spent on task as planned"
        } else {
            "You've spent ${-result} more than planned"
        }
    }

    private fun tasksDone(tasks: List<Task>): Int {
        val average = tasks.filter { it.taskStatus == TaskStatus.DONE }.count().toDouble() / tasks.count()
        return (100 * average).toInt()
    }

    private fun showGraph(tasks: List<Task>) {
        val taskForDates = tasks.groupBy {
            val cal = Calendar.getInstance()
            cal.time = it.taskDateStart
            cal.get(Calendar.DAY_OF_MONTH)
        }
        val labels = taskForDates.keys.toList()
        val done = mutableListOf<Number>()
        val abandon = mutableListOf<Number>()
        for (key in labels) {
            val t = taskForDates[key]
            t?.let {
                done.add(t.filter { it.taskStatus == TaskStatus.DONE }.count())
                abandon.add(t.filter { it.taskStatus == TaskStatus.ABANDONED }.count())
            }

        }
        val ser1 = SimpleXYSeries(
            done, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Done"
        )
        val ser2 = SimpleXYSeries(
            abandon, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Abandon"
        )

        val series1Format = MyFormatter(Color.RED, Color.BLUE)

        val series2Format = MyFormatter(Color.BLUE, Color.RED)

        fragmentStatsPlot.addSeries(ser1, series1Format)
        fragmentStatsPlot.addSeries(ser2, series2Format)
        fragmentStatsPlot.rangeStepModel = StepModel(StepMode.INCREMENT_BY_VAL, 1.0)
        fragmentStatsPlot.linesPerDomainLabel = labels.count()
        fragmentStatsPlot.linesPerRangeLabel = labels.count()
        fragmentStatsPlot.domainStepModel = StepModel(StepMode.INCREMENT_BY_VAL, 1.0)
        fragmentStatsPlot.setRangeLowerBoundary(0, BoundaryMode.FIXED)
        val maxHeight = Math.max((done as MutableList<Int>).max()?:0, (abandon as MutableList<Int>).max()?:0)
        fragmentStatsPlot.setRangeUpperBoundary(maxHeight + 0.5, BoundaryMode.FIXED)

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

//        renderer.setBarGroupWidth(BarRenderer.BarGroupWidthMode.FIXED_WIDTH, 10f)
        renderer.barOrientation = BarRenderer.BarOrientation.SIDE_BY_SIDE
        fragmentStatsPlot.invalidate()
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