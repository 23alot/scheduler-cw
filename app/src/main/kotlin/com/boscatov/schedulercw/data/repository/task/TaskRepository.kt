package com.boscatov.schedulercw.data.repository.task

import com.boscatov.schedulercw.data.entity.Task
import java.util.Date

interface TaskRepository {
    fun getTasks(): List<Task>

    fun getDateTask(date: Date): List<Task>

    fun getNearestTask(): Task?

    fun saveTask(task: Task)

    fun getLatestTask(taskStatus: IntArray): Task?
}