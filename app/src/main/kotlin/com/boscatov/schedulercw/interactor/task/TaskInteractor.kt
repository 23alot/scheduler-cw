package com.boscatov.schedulercw.interactor.task

import com.boscatov.schedulercw.data.entity.Task
import com.boscatov.schedulercw.data.entity.TaskStatus
import io.reactivex.Observable
import java.util.Date

interface TaskInteractor {
    // TODO: Переделать на observable
    fun getTask(taskId: Long): Task

    fun getTasks(): List<Task>

    fun getTasks(taskStatus: Array<TaskStatus>): Observable<List<Task>>

    fun getDateTasks(date: Date): List<Task>

    fun getNearestTask(): Task?

    fun getNearestTask(taskStatus: Array<TaskStatus>): Task?

    fun saveTask(task: Task)

    fun updateTask(task: Task)

    fun getLatestTask(taskStatus: Array<TaskStatus>): Task?
}