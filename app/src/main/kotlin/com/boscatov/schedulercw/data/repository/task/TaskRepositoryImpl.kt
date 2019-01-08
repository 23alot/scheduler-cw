package com.boscatov.schedulercw.data.repository.task

import android.graphics.Color
import com.boscatov.schedulercw.data.entity.Task
import com.boscatov.schedulercw.data.source.database.task.TaskDatabase
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor() : TaskRepository {
    @Inject
    lateinit var database: TaskDatabase

    override fun getTasks(): List<Task> {
        return database.taskDao().getAll()
    }

    override fun getDateTask(date: Date): List<Task> {
        val start = Calendar.getInstance()
        start.time = Date(date.time)
        start.set(Calendar.HOUR_OF_DAY, 0)
        start.set(Calendar.MINUTE, 0)
        start.set(Calendar.SECOND, 0)
        val end = Calendar.getInstance()
        end.time = Date(date.time)
        end.set(Calendar.HOUR_OF_DAY, 23)
        end.set(Calendar.MINUTE, 59)
        end.set(Calendar.SECOND, 59)
        return database.taskDao().loadAllByDate(start.time, end.time)
    }

    override fun getNearestTask(): Task? {
        val start = Calendar.getInstance()
        val end = Calendar.getInstance()
        end.add(Calendar.DAY_OF_MONTH, 7)
        end.set(Calendar.HOUR_OF_DAY, 23)
        end.set(Calendar.MINUTE, 59)
        end.set(Calendar.SECOND, 59)
        return database.taskDao().getNearestTask(start.time, end.time)
    }

    override fun saveTask(task: Task) {
        database.taskDao().insertAll(task)
    }
}