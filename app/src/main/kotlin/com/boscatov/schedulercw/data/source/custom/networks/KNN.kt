package com.boscatov.schedulercw.data.source.custom.networks

import com.boscatov.schedulercw.data.entity.Task
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.Comparator

class KNN(
    tasks: List<Task>,
    private val params: List<Double>,
    private val k: Int) {
    private val dataset = arrayListOf<Data>()

    init {
        for (task in tasks) {
            if (task.taskDateStart == null) continue
            val v1 = task.taskDuration
            val v2 = task.taskPriority
            dataset.add(Data(v1, v2, normalizeDate(task.taskDateStart!!.time)))
        }
    }

    private fun normalizeDate(date: Long): Long {
        val d = Date(date)
        val cal = Calendar.getInstance()
        cal.time = d
        cal.set(Calendar.YEAR, 1970)
        cal.set(Calendar.MONTH, Calendar.JANUARY)
        return cal.time.time
    }

    fun predict(task: Task): List<Long> {
        val v1 = task.taskDuration
        val v2 = task.taskPriority
        val distances = arrayListOf<Pair<Double, Long>>()
        for (d in dataset) {
            distances.add(Pair(calculateDistance(v1, v2, d), d.ans))
        }
        distances.sortWith(Comparator { a, b -> compareValues(a.first, b.first) })

        val map = mutableMapOf<Long, Int>()
        for (i in 0 until k) {
            if (i >= distances.count()) break

            if (map[distances[i].second] != null) {
                map[distances[i].second] = map[distances[i].second]?.plus(1)?:1
            } else {
                map[distances[i].second] = 1
            }
        }
        val result = map.toList().sortedBy { (_, value) -> value }.map { it.first }

        return result
    }

    private fun calculateDistance(v1: Int, v2: Int, d: Data): Double {
        val a1 = (v1 - d.v1) * (v1 - d.v1) * params[0]
        val a2 = (v2 - d.v2) * (v2 - d.v2) * params[1]
        return Math.sqrt(a1 + a2)
    }

    data class Data(val v1: Int, val v2: Int, val ans: Long)
}