package com.boscatov.schedulercw.data.repository.neuralnetwork

import com.boscatov.matrix.Matrix
import com.boscatov.matrix.matrixOf
import com.boscatov.schedulercw.data.entity.Task
import java.util.*
import javax.inject.Inject

/**
 * Created by boscatov on 08.03.2019.
 */

class NeuralNetworkRepositoryImpl @Inject constructor(): NeuralNetworkRepository {
    private var X = matrixOf()
    private val y = matrixOf()
    private val mean = mutableListOf<Double>()
    private val s = mutableListOf<Double>()
    private lateinit var w: Matrix
    override fun fit(tasks: List<Task>) {
        val x = mutableListOf<MutableList<Double>>()
        val Y = mutableListOf<Double>()
        tasks.forEach {
            val calendar = Calendar.getInstance()
            calendar.time = it.taskDateStart
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH).toDouble()
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK).toDouble()
            val hour = calendar.get(Calendar.HOUR_OF_DAY).toDouble()
            val minute = calendar.get(Calendar.MINUTE).toDouble()
            val duration = it.taskDuration.toDouble()
            val priority = it.taskPriority.toDouble()
            x.add(mutableListOf(duration, priority, dayOfMonth, dayOfWeek))
            Y.add(60 * hour + minute)
        }
        superML(x, Y)
    }

    override fun predict(task: Task): Date {
        val calendar = Calendar.getInstance()
        calendar.time = task.taskDateStart
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH).toDouble()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK).toDouble()
        val duration = task.taskDuration.toDouble()
        val priority = task.taskPriority.toDouble()

        val input = matrixOf()
        input.addRow(
            mutableListOf(
                duration,
                priority,
                dayOfMonth,
                dayOfWeek
            )
        )

        val result = (input * w).toDoubleArrayList()[0]

        calendar.set(Calendar.HOUR_OF_DAY, (result / 60).toInt())
        calendar.set(Calendar.MINUTE, result.toInt() % 60)
        return calendar.time
    }


    /**
     * Используется z-score normalization
     */
    private fun normalize() {
        mean.clear()
        this.s.clear()
        val XT = X.T
        val matrix = matrixOf()
        for (row in XT) {
            var s = 0.0
            var verh = 0.0
            val count = row.count()
            row.forEach { s += it }
            s /= count
            row.forEach { verh += Math.pow(it - s, 2.0) }
            var result = Math.sqrt(verh / count)
            if (result == 0.0) {
                result = 1.0
            }

            matrix.addRow(row.map { (it - s) / result }.toMutableList())
            mean.add(s)
            this.s.add(result)
        }
        //TODO: Веса становятся NaN скорее всего в inverse det=0
        X = matrix.T
    }

    private fun superML(X: List<List<Double>>, y: List<Double>) {
        val we = mutableListOf<Double>()
        for (i in 0..4) we.add(0.0)
        var e = 1.5
        val alpha = 0.5
        while (e > 5.0) {
            val weights = mutableListOf<Double>()
            weights.addAll(we)
            we[0] -= alpha * calculate(X, y, weights, 0)
            we[1] -= alpha * calculate(X, y, weights, 1)
            e = Math.sqrt(Math.pow(we[0] - weights[0], 2.0) + Math.pow(we[1] - weights[1], 2.0))
        }
    }

    private fun calculate(X: List<List<Double>>, y: List<Double>, w: List<Double>, pos: Int): Double {
        var sum = 0.0
        for ((i, xrow) in X.withIndex()) {
            sum += 2 * w[pos] * xrow[pos] * xrow[pos] + 2 * w[Math.abs(pos - 1)] * xrow[0] * xrow [1] * 2 * xrow[pos] * y[i]
        }
        sum /= X.count()
        sum += 2 * 0.5 * w[pos]
        return sum
    }
}