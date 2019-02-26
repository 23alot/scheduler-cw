package com.boscatov.schedulercw.interactor.scheduler

import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.boscatov.schedulercw.data.entity.TaskStatus
import com.boscatov.schedulercw.data.repository.task.TaskRepository
import com.boscatov.schedulercw.di.Scopes
import com.boscatov.schedulercw.worker.ActiveTaskWorker
import io.reactivex.Completable
import toothpick.Toothpick
import javax.inject.Inject

class SchedulerInteractorImpl : SchedulerInteractor {
    @Inject
    lateinit var taskRepository: TaskRepository

    init {
        val scope = Toothpick.openScope(Scopes.TASK_SCOPE)
        Toothpick.inject(this, scope)
    }

    override fun startTaskCompletable(taskId: Long): Completable {
        return Completable.create {
            val task = taskRepository.getTask(taskId)
            task.taskStatus = TaskStatus.ACTIVE
            taskRepository.saveTask(task)
            // Вызов ActiveTaskWorker
            val activeTaskWorker = OneTimeWorkRequestBuilder<ActiveTaskWorker>().build()
            WorkManager.getInstance().beginUniqueWork("ActiveTask", ExistingWorkPolicy.REPLACE, activeTaskWorker).enqueue()
        }
    }

    override fun completeTaskCompletable(taskId: Long): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun notifyTaskShouldBeEndedCompletable(taskId: Long): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun abandonTaskCompletable(taskId: Long): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}