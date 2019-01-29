package com.boscatov.schedulercw.networks

import com.boscatov.schedulercw.data.entity.Task

class KNN(tasks: List<Task>, private val params: List<Double>, private val k: Int) {
    private val dataset = arrayListOf<Data>()

    init {
        for (task in tasks) {
            val v1 = task.taskDuration
            val v2 = task.taskPriority
            dataset.add(Data(v1, v2, (Math.random() * 1000).toLong()))
        }
    }

    fun predict(task: Task) {
        val v1 = task.taskDuration
        val v2 = task.taskPriority
        val distances = arrayListOf<Pair<Double, Long>>()
        for (d in dataset) {
            distances.add(Pair(calculateDistance(v1, v2, d), d.ans))
        }
        distances.sortWith(Comparator { a, b -> compareValues(a.first, b.first) })
        // TODO: Поиск наибольшего числа соседей
        val map = mutableMapOf<Long, Int>()
        for (i in 0 until k) {
            if (map[distances[i].second] != null) {
                map[distances[i].second]?.inc()
            } else {
                map[distances[i].second] = 0
            }
        }
        map.toList().sortedBy { (_, value) -> value }[0]
    }

    private fun calculateDistance(v1: Int, v2: Int, d: Data): Double {
        val a1 = (v1 - d.v1) * (v1 - d.v1) * params[0]
        val a2 = (v2 - d.v2) * (v2 - d.v2) * params[1]
        return Math.sqrt(a1 + a2)
    }

    data class Data(val v1: Int, val v2: Int, val ans: Long)
}