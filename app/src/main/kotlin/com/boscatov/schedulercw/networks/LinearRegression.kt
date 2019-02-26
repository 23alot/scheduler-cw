package com.boscatov.schedulercw.networks

import android.graphics.Color
import android.util.Log
import com.boscatov.matrix.Matrix
import com.boscatov.schedulercw.data.entity.Task
import java.util.Calendar

class LinearRegression {
    fun calculateWeights(dataset: ArrayList<Pair<ArrayList<Double>, Double>>): ArrayList<Double> {
        val X = Matrix()
        val y = Matrix()
        for (data in dataset) {
            X.addRow(data.first)
            y.addRow(arrayListOf(data.second))
        }
        val XT = X.T
        val weights = (XT * X).inverse() * XT * y
        return weights.toDoubleArrayList()
    }

    // Пока без загрузки в бд
    fun generateTestSample(): ArrayList<Task> {
        val result = arrayListOf<Task>()
        val date = Calendar.getInstance()
        for (i in 0 until 100000) {
            date.add(Calendar.HOUR, (Math.random() * 50).toInt())
            val task = Task(
                taskTitle = "123",
                taskDescription = "456",
                taskColor = Color.RED,
                taskDateStart = date.time,
                taskPriority = (Math.random() * 10).toInt() % 5,
                taskDuration = (Math.random() * 370).toInt() % 120
            )
            result.add(task)
        }

        return result
    }

    fun parseTasks(tasks: List<Task>): ArrayList<Pair<ArrayList<Double>, Double>> {
        val values = arrayListOf<Pair<ArrayList<Double>, Double>>()
        for (task in tasks) {
            values.add(task.castToNNvalues())
        }
        return values
    }

    fun start() {
        var startTime = System.currentTimeMillis()
        val testSample = generateTestSample()
        println("Sample generate ${System.currentTimeMillis() - startTime}")
        startTime = System.currentTimeMillis()
        val values = parseTasks(testSample)
        println("Sample parse ${System.currentTimeMillis() - startTime}")
        startTime = System.currentTimeMillis()
        val result = calculateWeights(values)
        println("Sample calculate ${System.currentTimeMillis() - startTime}\n$result")
    }
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val ll = LinearRegression()
            ll.start()
        }
    }

}

