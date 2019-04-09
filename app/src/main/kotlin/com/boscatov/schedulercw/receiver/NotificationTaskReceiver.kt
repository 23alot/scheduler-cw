package com.boscatov.schedulercw.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.boscatov.schedulercw.data.entity.TASK_ACTION
import com.boscatov.schedulercw.data.entity.TaskAction
import com.boscatov.schedulercw.data.entity.TaskStatus
import com.boscatov.schedulercw.di.Scopes
import com.boscatov.schedulercw.interactor.scheduler.SchedulerInteractor
import com.boscatov.schedulercw.interactor.task.TaskInteractor
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import toothpick.Toothpick
import javax.inject.Inject

class NotificationTaskReceiver : BroadcastReceiver() {
    @Inject
    lateinit var schedulerInteractor: SchedulerInteractor

    @Inject
    lateinit var taskInteractor: TaskInteractor

    init {
        val scope = Toothpick.openScope(Scopes.TASK_SCOPE)
        Toothpick.inject(this, scope)
    }

    override fun onReceive(context: Context, intent: Intent?) {
        intent?.let {
            val action = it.getIntExtra(TASK_ACTION, -1)

            if (action != -1) {
                val taskAction = TaskAction.values()[action]
                Observable.fromCallable {
                    val task = taskInteractor.getLatestTask(arrayOf(TaskStatus.ACTIVE, TaskStatus.PENDING))?:return@fromCallable
                    when (taskAction) {
                        TaskAction.START -> schedulerInteractor.startTaskCompletable(task.taskId).subscribe()
                        TaskAction.FINISH -> schedulerInteractor.completeTaskCompletable(task.taskId).subscribe()
                        TaskAction.ABANDON -> schedulerInteractor.abandonTaskCompletable(task.taskId).subscribe()
                    }
                    val updateIntent = Intent("com.boscatov.schedulercw.updatelist")
                    LocalBroadcastManager.getInstance(context).sendBroadcast(updateIntent)
                }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {

                }


            }
        }
    }



}