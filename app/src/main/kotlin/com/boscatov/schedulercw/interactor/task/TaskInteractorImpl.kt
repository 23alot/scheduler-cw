package com.boscatov.schedulercw.interactor.task

import com.boscatov.schedulercw.data.entity.Task
import com.boscatov.schedulercw.data.entity.TaskStatus
import com.boscatov.schedulercw.data.repository.task.TaskRepository
import com.boscatov.schedulercw.di.Scopes
import toothpick.Toothpick
import java.util.Date
import javax.inject.Inject

class TaskInteractorImpl : TaskInteractor {

    @Inject
    lateinit var taskRepository: TaskRepository

    init {
        val scope = Toothpick.openScope(Scopes.TASK_SCOPE)
        Toothpick.inject(this, scope)
    }

    override fun getTasks(): List<Task> {
        return taskRepository.getTasks()
    }

    override fun getDateTasks(date: Date): List<Task> {
        return taskRepository.getDateTask(date)
    }

    override fun getNearestTask(): Task? {
        return taskRepository.getNearestTask()
    }

    override fun saveTask(task: Task) {
        taskRepository.saveTask(task)
    }

    override fun getLatestTask(taskStatus: Array<TaskStatus>): Task? {
        val intStatus = IntArray(taskStatus.size) {
            taskStatus[it].ordinal
        }
        return taskRepository.getLatestTask(intStatus)
    }
}