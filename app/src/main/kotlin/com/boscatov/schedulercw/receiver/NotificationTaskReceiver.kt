package com.boscatov.schedulercw.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.boscatov.schedulercw.data.entity.TASK_ACTION
import com.boscatov.schedulercw.data.entity.TaskAction
import com.boscatov.schedulercw.di.Scopes
import com.boscatov.schedulercw.interactor.scheduler.SchedulerInteractor
import toothpick.Toothpick
import javax.inject.Inject

class NotificationTaskReceiver : BroadcastReceiver() {
    @Inject
    lateinit var schedulerInteractor: SchedulerInteractor

    init {
        val scope = Toothpick.openScope(Scopes.TASK_SCOPE)
        Toothpick.inject(this, scope)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            val action = it.extras?.getInt(TASK_ACTION)
            action?.let{
                val taskAction = TaskAction.values()[action]

            }
        }
    }

}