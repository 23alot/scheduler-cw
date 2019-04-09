package com.boscatov.schedulercw.data.repository.task

import com.boscatov.schedulercw.data.entity.Task
import java.util.Date

interface TaskRepository {

    fun getTask(taskId: Long): Task

    fun getTasks(): List<Task>

    fun getTasks(date: Date): List<Task>

    fun getTasks(taskStatus: IntArray): List<Task>

    fun getDateTask(date: Date): List<Task>

    fun getNearestTask(): Task?

    fun getNearestTask(taskStatus: IntArray): Task?

    fun saveTask(task: Task)

    fun updateTask(task: Task)

    fun getLatestTask(taskStatus: IntArray): Task?
}