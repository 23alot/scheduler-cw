package com.boscatov.schedulercw.interactor.task

import com.boscatov.schedulercw.data.entity.Task

interface TaskInteractor {
    fun getTasks(): List<Task>

    fun getNearestTask(): Task?
}