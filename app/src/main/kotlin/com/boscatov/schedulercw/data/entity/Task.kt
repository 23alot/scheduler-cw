package com.boscatov.schedulercw.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true) val taskId: Long = 0,
    @ColumnInfo(name = "task_title") val taskTitle: String,
    @ColumnInfo(name = "task_description") val taskDescription: String,
    @ColumnInfo(name = "task_color") val taskColor: Int,
    @ColumnInfo(name = "task_duration") val taskDuration: Int,
    @ColumnInfo(name = "task_date_start") val taskDateStart: String,
    @ColumnInfo(name = "task_time_start") val taskTimeStart: String,
    @ColumnInfo(name = "task_priority") val taskPriority: Int,
    @ColumnInfo(name = "task_is_done") val taskIsDone: Boolean = false
)