package com.boscatov.schedulercw.data.repository.neuralnetwork

import com.boscatov.matrix.matrixOf
import com.boscatov.schedulercw.data.entity.Task
import java.util.*

/**
 * Created by boscatov on 08.03.2019.
 */

class NeuralNetworkRepositoryImpl : NeuralNetworkRepository {
    private val matrix = matrixOf()
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

            matrix.addRow(
                mutableListOf(
                    duration,
                    priority
                )
            )

            normalize()
        }
    }

    override fun predict(task: Task): Date {
        return Date()
    }

    private fun normalize() {

    }
}