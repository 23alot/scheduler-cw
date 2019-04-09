package com.boscatov.schedulercw.worker

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.boscatov.schedulercw.Actions
import com.boscatov.schedulercw.R
import com.boscatov.schedulercw.data.entity.TASK_ACTION
import com.boscatov.schedulercw.data.entity.TaskAction
import com.boscatov.schedulercw.data.entity.TaskStatus
import com.boscatov.schedulercw.di.Scopes
import com.boscatov.schedulercw.interactor.scheduler.SchedulerInteractor
import com.boscatov.schedulercw.interactor.task.TaskInteractor
import com.boscatov.schedulercw.receiver.NotificationTaskReceiver
import io.reactivex.disposables.Disposable
import io.reactivex.internal.operators.observable.ObservableInterval
import io.reactivex.schedulers.Schedulers
import toothpick.Toothpick
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ActiveTaskWorker(
    private val context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    @Inject
    lateinit var taskInteractor: TaskInteractor

    @Inject
    lateinit var schedulerInteractor: SchedulerInteractor

    init {
        val scope = Toothpick.openScope(Scopes.TASK_SCOPE)
        Toothpick.inject(this, scope)
    }

    private var disposable: Disposable? = null

    override fun doWork(): Result {
        val activeTask = taskInteractor.getLatestTask(arrayOf(TaskStatus.ACTIVE))
        val taskId = activeTask?.taskId
        val remoteViews = RemoteViews(context.packageName, R.layout.notification_progress)
        remoteViews.setTextViewText(R.id.notificationProgressTitleTV, activeTask?.taskTitle)
        remoteViews.setTextViewText(
            R.id.notificationProgressTimeTV, "0"
        )
        val intent = Intent(Actions.NOTIFICATION_TASK).also {
            it.putExtra(TASK_ACTION, TaskAction.FINISH.ordinal)
            it.setClass(context, NotificationTaskReceiver::class.java)
        }

        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        remoteViews.setOnClickPendingIntent(R.id.notificationProgressIB, pendingIntent)

        val intent2 = Intent(Actions.NOTIFICATION_TASK).also {
            it.putExtra(TASK_ACTION, TaskAction.ABANDON.ordinal)
            it.setClass(context, NotificationTaskReceiver::class.java)
        }

        val pendingIntent2 = PendingIntent.getBroadcast(context, 1, intent2, PendingIntent.FLAG_UPDATE_CURRENT)
        remoteViews.setOnClickPendingIntent(R.id.notificationProgressCancelIB, pendingIntent2)


        updateNotification(remoteViews)

        disposable = ObservableInterval(0L, 1L, TimeUnit.SECONDS, Schedulers.computation())
            .subscribe {
                taskId?.let { id ->
                    val task = taskInteractor.getTask(id)
                    if (task.taskStatus != TaskStatus.ACTIVE) {
                        val nearestTaskBuilder = OneTimeWorkRequestBuilder<NearestTaskWorker>()
                        WorkManager.getInstance().enqueue(nearestTaskBuilder.build())
                        disposable?.dispose()
                    }
                } ?: run {
                    val nearestTaskBuilder = OneTimeWorkRequestBuilder<NearestTaskWorker>()
                    WorkManager.getInstance().enqueue(nearestTaskBuilder.build())
                    disposable?.dispose()
                }

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