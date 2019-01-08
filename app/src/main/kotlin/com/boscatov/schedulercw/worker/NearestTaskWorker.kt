package com.boscatov.schedulercw.worker

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.boscatov.schedulercw.R
import com.boscatov.schedulercw.data.entity.Task
import com.boscatov.schedulercw.di.Scopes
import com.boscatov.schedulercw.interactor.task.TaskInteractor
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
//        sendNotification(taskInteractor.getNearestTask())
        sendNotificationStart()
        return Result.success()
    }

    private fun sendNotification(task: Task?) {
        createNotificationChannel()
        Log.d("NearestTaskWorker", "$task ${task?.taskTitle}")
        val title = task?.taskTitle ?: "No tasks"
        val description = StringBuilder()
        description.append(task?.taskDescription).append(" ")
        description.append(SimpleDateFormat("HH:mm").format(task?.taskDateStart)).append(" ")
        description.append(task?.taskDuration)
        val builder = NotificationCompat.Builder(context, PERIODIC_TASK_NOTIFICATION_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(description.toString())
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            val notification = builder.build()
            notification.flags = Notification.FLAG_ONGOING_EVENT
            notify(TASK_MONITOR_ID, builder.build())
        }
    }

    private fun sendNotificationStart() {
        val remoteViews = RemoteViews(context.packageName, R.layout.notification_start)
        remoteViews.setTextViewText(R.id.notificationStartTitleTV, "Description")
        remoteViews.setTextViewText(R.id.notificationStartTimeTV, "16:00")
        val builder = NotificationCompat.Builder(context, PERIODIC_TASK_NOTIFICATION_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setCustomContentView(remoteViews)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            val notification = builder.build()
            notification.flags = Notification.FLAG_ONGOING_EVENT
            notify(TASK_MONITOR_ID, builder.build())
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Periodic checker"
            val descriptionText = "Check nearest task"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(PERIODIC_TASK_NOTIFICATION_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val PERIODIC_TASK_NOTIFICATION_ID = "1"
        const val TASK_MONITOR_ID = 674
    }
}