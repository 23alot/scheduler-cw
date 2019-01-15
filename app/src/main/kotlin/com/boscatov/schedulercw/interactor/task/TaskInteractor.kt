package com.boscatov.schedulercw.interactor.task

import com.boscatov.schedulercw.data.entity.Task
import com.boscatov.schedulercw.data.entity.TaskStatus
import java.util.Date

interface TaskInteractor {
    fun getTasks(): List<Task>

    fun getDateTasks(date: Date): List<Task>

    fun getNearestTask(): Task?

    fun saveTask(task: Task)

    fun getLatestTask(taskStatus: Array<TaskStatus>): Task?
}