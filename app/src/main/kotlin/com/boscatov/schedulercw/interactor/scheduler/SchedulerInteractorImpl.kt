package com.boscatov.schedulercw.interactor.scheduler

import android.util.Log
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.boscatov.schedulercw.data.entity.TaskStatus
import com.boscatov.schedulercw.data.repository.task.TaskRepository
import com.boscatov.schedulercw.di.Scopes
import com.boscatov.schedulercw.worker.ActiveTaskWorker
import com.boscatov.schedulercw.worker.NearestTaskWorker
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import toothpick.Toothpick
import javax.inject.Inject

class SchedulerInteractorImpl : SchedulerInteractor {

    companion object {
        private const val TAG = "SchedulerInteractorImpl"
    }

    @Inject
    lateinit var taskRepository: TaskRepository

    init {
        val scope = Toothpick.openScope(Scopes.TASK_SCOPE)
        Toothpick.inject(this, scope)
    }

    override fun startTaskCompletable(taskId: Long): Completable {
        Log.d(TAG, "start task $taskId")
        return Completable.create {
            val task = taskRepository.getTask(taskId)
            task.taskStatus = TaskStatus.ACTIVE
            taskRepository.updateTask(task)
            // Вызов ActiveTaskWorker
            val activeTaskWorker = OneTimeWorkRequestBuilder<ActiveTaskWorker>().build()
            WorkManager.getInstance().enqueueUniqueWork("ActiveTask", ExistingWorkPolicy.REPLACE, activeTaskWorker)
        }.subscribeOn(Schedulers.io())
    }

    override fun completeTaskCompletable(taskId: Long): Completable {
        Log.d(TAG, "complete task $taskId")
        return Completable.create {
            val task = taskRepository.getTask(taskId)
            task.taskStatus = TaskStatus.DONE
            taskRepository.updateTask(task)
            val nearestTaskWorker = OneTimeWorkRequestBuilder<NearestTaskWorker>().build()
            WorkManager.getInstance().enqueueUniqueWork("CompleteTask", ExistingWorkPolicy.REPLACE, nearestTaskWorker)
        }.subscribeOn(Schedulers.io())
    }

    override fun notifyTaskShouldBeEndedCompletable(taskId: Long): Completable {
        return Completable.create {
            val task = taskRepository.getTask(taskId)
            task.taskStatus = TaskStatus.WAIT_FOR_ACTION
            taskRepository.updateTask(task)
        }.subscribeOn(Schedulers.io())
    }

    override fun abandonTaskCompletable(taskId: Long): Completable {
        Log.d(TAG, "abandon task $taskId")
        return Completable.create {
            val task = taskRepository.getTask(taskId)
            task.taskStatus = TaskStatus.ABANDONED
            task.taskDateStart = null
            taskRepository.updateTask(task)
            val nearestTaskWorker = OneTimeWorkRequestBuilder<NearestTaskWorker>().build()
            WorkManager.getInstance().enqueueUniqueWork("AbandonTask", ExistingWorkPolicy.REPLACE, nearestTaskWorker)
        }.subscribeOn(Schedulers.io())
    }

}