package com.boscatov.schedulercw.data.repository.scheduler_algorithm

import com.boscatov.schedulercw.data.entity.TaskDate
import java.util.*
import javax.inject.Inject

/**
 * Created by boscatov on 08.03.2019.
 */
class SchedulerAlgorithmRepositoryImpl @Inject constructor() : SchedulerAlgorithmRepository {

    override fun schedule(tasks: List<SchedulerData>, reserved: List<ReservedData>): List<SchedulerData> {
        val data = mutableListOf<SchedulerData>()
        data.addAll(tasks)
        val reserve = mutableListOf<ReservedData>()
        reserve.addAll(reserved)
        reserve.sortBy { it.startTime }
        val comparator = SchedulerComparator()
        data.sortWith(comparator)
        val currentBest = mutableListOf<ReservedData>()
        val predict = predictTime(0, data, reserve, currentBest)
        if (!predict) {
            scheduleOther((currentBest.count() - reserve.count()), data, currentBest)
            for (task in tasks) {
                val result = currentBest.find { it.schedulerData == task }
                result?.let {
                    task.resultTime = it.startTime
                }
            }
        } else {
            for (task in tasks) {
                val result = reserve.find { it.schedulerData == task }
                result?.let {
                    task.resultTime = it.startTime
                }
            }
        }

        return tasks
    }

    private fun predictTime(
        pos: Int,
        tasks: List<SchedulerData>,
        reserved: MutableList<ReservedData>,
        currentBest: MutableList<ReservedData>
    ): Boolean {
        if (pos >= tasks.count()) {
            return true
        }
        val task = tasks[pos]
        for (desiredTime in task.desiredTimes) {
            for ((i, reserve) in reserved.withIndex()) {
                if (reserve.startTime > desiredTime && i > 0) {
                    val previous = reserved[i - 1]
                    val next = reserved[i]
                    if (desiredTime >= previous.endTime && desiredTime + task.duration <= next.startTime) {
                        reserved.add(i, ReservedData(desiredTime, desiredTime + task.duration, task))
                        if (predictTime(pos + 1, tasks, reserved, currentBest)) {
                            task.resultTime = desiredTime
                            return true
                        } else {
                            reserved.removeAt(i)
                            break
                        }
                    }
                }
            }
        }

        if (pos + reserved.count() > currentBest.count() + 1) {
            currentBest.clear()
            currentBest.addAll(reserved)
        }

        return false
    }

    private fun scheduleOther(
        pos: Int,
        tasks: List<SchedulerData>,
        currentBest: MutableList<ReservedData>
    ): Boolean {
        if (pos == tasks.count()) {
            return true
        }
        val task = tasks[pos]
        val time = task.desiredTimes[0]
        val window = task.duration
        var best = ReservedData(-5, -5)
        for ((i, _) in currentBest.withIndex()) {
            if (i + 1 == currentBest.count()) {
                continue
            }
            if (currentBest[i + 1].startTime - currentBest[i].endTime >= window) {
                if (time < currentBest[i + 1].startTime) {
                    if (time > currentBest[i].endTime) {
                        val currentDif = Math.abs(best.startTime - time)
                        val newDif = Math.abs(time)
                        if (currentDif > newDif) {
                            best = ReservedData(time, time + task.duration, task)
                        }
                    } else {
                        val currentDif = Math.abs(best.startTime - time)
                        val newDif = Math.abs(currentBest[i].endTime - time)
                        if (currentDif > newDif) {
                            best = ReservedData(currentBest[i].endTime, currentBest[i].endTime + task.duration, task)
                        }
                    }
                } else {
                    val currentDif = Math.abs(best.startTime - time)
                    val newDif = Math.abs((currentBest[i + 1].startTime - task.duration) - time)
                    if (currentDif > newDif) {
                        best = ReservedData(
                            currentBest[i + 1].startTime - task.duration,
                            currentBest[i + 1].startTime,
                            task
                        )
                    }
                }
            }
        }

        return if (!(best.startTime == 0L && best.endTime == 0L)) {
            val index = currentBest.indexOfFirst { it.startTime >= best.endTime }
            currentBest.add(index, best)

            scheduleOther(pos + 1, tasks, currentBest)
        } else {
            false
        }
    }
}

class SchedulerComparator : Comparator<SchedulerData> {
    override fun compare(o1: SchedulerData, o2: SchedulerData): Int {
        return when {
            o1.deadline < o2.deadline -> -1
            o1.deadline == o2.deadline -> when {
                o1.priority > o2.priority -> -1
                o1.priority < o2.priority -> 1
                o1.duration < o2.duration -> 1
                else -> -1
            }
            else -> 1
        }
    }
}

data class ReservedData(
    val startTime: Long,
    val endTime: Long,
    val schedulerData: SchedulerData? = null
)

data class SchedulerData(
    val priority: Int,
    val duration: Long,
    val desiredTimes: List<Long>,
    val deadline: Long,
    var resultTime: Long = 0
)