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
        tasks.forEach {
            val calendar = Calendar.getInstance()
            calendar.time = it.taskDateStart
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH).toDouble()
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK).toDouble()
            val hour = calendar.get(Calendar.HOUR_OF_DAY).toDouble()
            val minute = calendar.get(Calendar.MINUTE).toDouble()
            val duration = it.taskDuration.toDouble()
            val priority = it.taskPriority.toDouble()

            X.addRow(
                mutableListOf(
                    duration,
                    priority,
                    dayOfMonth,
                    dayOfWeek
                )
            )

            y.addRow(
                mutableListOf(
                    60 * hour + minute
                )
            )


        }

        normalize()

        val XT = X.T
        w = (XT * X).inverse() * XT * y
    }

    override fun predict(task: Task): Date {
        val calendar = Calendar.getInstance()
        calendar.time = task.taskDateStart
        val dayOfMonth = (calendar.get(Calendar.DAY_OF_MONTH).toDouble() - mean[2]) / s[2]
        val dayOfWeek = (calendar.get(Calendar.DAY_OF_WEEK).toDouble() - mean[3]) / s[3]
        val duration = (task.taskDuration.toDouble() - mean[0]) / s[0]
        val priority = (task.taskPriority.toDouble() - mean[1]) / s[1]

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
}