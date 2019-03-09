package com.boscatov.schedulercw.worker

import android.app.Notification
import android.content.Context
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.boscatov.schedulercw.R
import com.boscatov.schedulercw.data.entity.Task
import io.reactivex.internal.operators.observable.ObservableInterval
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class ActiveTaskWorker(
    private val context: Context,
    params: WorkerParameters,
    private val activeTask: Task
) :
    Worker(context, params) {
    override fun doWork(): Result {
        val remoteViews = RemoteViews(context.packageName, R.layout.notification_progress)
        remoteViews.setTextViewText(R.id.notificationProgressTitleTV, activeTask.taskTitle)
        remoteViews.setTextViewText(
            R.id.notificationProgressTimeTV, "0"
        )
        updateNotification(remoteViews)
        ObservableInterval(0L, 1L, TimeUnit.SECONDS, Schedulers.computation()).subscribe {
            remoteViews.setTextViewText(
                R.id.notificationProgressTimeTV, "$it"
            )
            updateNotification(remoteViews)
        }

        return Result.success()
    }

    private fun updateNotification(remoteViews: RemoteViews) {
        val builder = NotificationCompat.Builder(context, NearestTaskWorker.PERIODIC_TASK_NOTIFICATION_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setCustomContentView(remoteViews)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            val notification = builder.build()
            notification.flags = Notification.FLAG_ONGOING_EVENT
            notify(NearestTaskWorker.TASK_MONITOR_ID, builder.build())
        }
    }

}