package com.boscatov.schedulercw.data.repository.task

import com.boscatov.schedulercw.data.entity.Task

interface TaskRepository {
    fun getTasks(): List<Task>

    fun getNearestTask(): Task?
}