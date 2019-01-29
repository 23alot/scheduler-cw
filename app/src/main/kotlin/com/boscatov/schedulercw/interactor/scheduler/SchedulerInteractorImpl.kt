package com.boscatov.schedulercw.interactor.scheduler

import com.boscatov.schedulercw.data.repository.task.TaskRepository
import com.boscatov.schedulercw.di.Scopes
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