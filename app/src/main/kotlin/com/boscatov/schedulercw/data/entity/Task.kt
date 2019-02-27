package com.boscatov.schedulercw.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar
import java.util.Date

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true) val taskId: Long = 0,
    @ColumnInfo(name = "task_title") val taskTitle: String,
    @ColumnInfo(name = "task_description") val taskDescription: String,
    @ColumnInfo(name = "task_color") val taskColor: Int,
    @ColumnInfo(name = "task_duration") val taskDuration: Int,
    @ColumnInfo(name = "task_date_start") val taskDateStart: Date,
    @ColumnInfo(name = "task_priority") val taskPriority: Int,
    @ColumnInfo(name = "task_is_done") val taskIsDone: Boolean = false,
    @ColumnInfo(name = "task_status") var taskStatus: TaskStatus = TaskStatus.PENDING
) {
    fun castToNNvalues(): Pair<ArrayList<Double>, Double> {
        val values = arrayListOf<Double>()
        values.add(taskDuration.toDouble())
        values.add(taskPriority.toDouble())
        return Pair(values, startDateToDouble())
    }

    private fun startDateToDouble(): Double {
        val value = Calendar.getInstance()
        value.time = taskDateStart
        val result = value.get(Calendar.DAY_OF_WEEK).toDouble() * value.get(Calendar.HOUR_OF_DAY) * value.get(Calendar.MINUTE)
        return result
    }
}

enum class TaskStatus {
    ABANDONED, WAIT_FOR_ACTION, DONE, ACTIVE, PENDING
}

enum class TaskAction {
    ABANDON, START, FINISH
}

const val TASK_ACTION = "TASK_ACTION"