package com.boscatov.schedulercw.worker

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.boscatov.schedulercw.R
import com.boscatov.schedulercw.data.entity.TASK_ACTION
import com.boscatov.schedulercw.data.entity.Task
import com.boscatov.schedulercw.data.entity.TaskAction
import com.boscatov.schedulercw.data.entity.TaskStatus
import com.boscatov.schedulercw.di.Scopes
import com.boscatov.schedulercw.interactor.task.TaskInteractor
import com.boscatov.schedulercw.receiver.NotificationTaskReceiver
import toothpick.Toothpick
import java.text.SimpleDateFormat
import javax.inject.Inject

class NearestTaskWorker(private val context: Context, params: WorkerParameters) : Worker(context, params) {

    @Inject
    lateinit var taskInteractor: TaskInteractor

    init {
        val scope = Toothpick.openScope(Scopes.TASK_SCOPE)
        Toothpick.inject(this, scope)
    }

    override fun doWork(): Result {
        sendNotificationStart(taskInteractor.getNearestTask())
        return Result.success()
    }

    private fun sendNotificationStart(task: Task?) {
//        if (task?.taskStatus == TaskStatus.ACTIVE) return
        val remoteViews: RemoteViews
        if (task != null) {
            remoteViews = RemoteViews(context.packageName, R.layout.notification_start)
            remoteViews.setTextViewText(R.id.notificationStartTitleTV, task.taskTitle)
            remoteViews.setTextViewText(
                R.id.notificationStartTimeTV,
                SimpleDateFormat("HH:mm").format(task.taskDateStart)
            )
            val intent = Intent("com.boscatov.schedulercw.action.NOTIFICATION_TASK").also { intent ->
                intent.putExtra(TASK_ACTION, TaskAction.START.ordinal)
                context.sendBroadcast(intent)
            }
            intent.setClass(context, NotificationTaskReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            remoteViews.setOnClickPendingIntent(R.id.notificationStartIB, pendingIntent)
            val intent2 = Intent("com.boscatov.schedulercw.action.NOTIFICATION_TASK").also { intent ->
                intent.putExtra(TASK_ACTION, TaskAction.ABANDON.ordinal)
                context.sendBroadcast(intent)
            }
            intent2.setClass(context, NotificationTaskReceiver::class.java)
            val pendingIntent2 = PendingIntent.getBroadcast(context, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT)
            remoteViews.setOnClickPendingIntent(R.id.notificationStartCancelIB, pendingIntent2)
        } else {
            remoteViews = RemoteViews(context.packageName, R.layout.notification_no_tasks)
            remoteViews.setTextViewText(R.id.notificationNoTaskTitleTV, "No recent tasks")
            remoteViews.setTextViewText(R.id.notificationNoTaskTimeTV, "You've done all tasks yet")
        }
        val builder = NotificationCompat.Builder(context, PERIODIC_TASK_NOTIFICATION_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setCustomContentView(remoteViews)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            val notification = builder.build()
            notification.flags = Notification.FLAG_ONGOING_EVENT
            notify(TASK_MONITOR_ID, notification)
        }
    }



    companion object {
        const val PERIODIC_TASK_NOTIFICATION_ID = "1"
        const val TASK_MONITOR_ID = 673
    }
}