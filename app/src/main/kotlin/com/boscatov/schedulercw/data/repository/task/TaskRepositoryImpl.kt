package com.boscatov.schedulercw.data.repository.task

import android.graphics.Color
import com.boscatov.schedulercw.data.entity.Task
import com.boscatov.schedulercw.data.source.database.task.TaskDatabase
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor() : TaskRepository {
    @Inject
    lateinit var database: TaskDatabase

    override fun getTasks(): List<Task> {
//        val test = arrayListOf<Task>()
//        val colors = listOf(Color.RED, Color.BLACK, Color.BLUE, Color.GREEN, Color.YELLOW, Color.GRAY)
//        for (i in 0..5) {
//            val task = Task(
//                i.toLong(),
//                taskTitle = "TestTitle$i",
//                taskDescription = "TestDescription$i",
//                taskDateStart = "TestDateStart$i",
//                taskDuration = i,
//                taskColor = colors[i],
//                taskTimeStart = "${i + 5}:00",
//                taskPriority = i
//            )
//            test.add(task)
//        }
//        return test
        return database.taskDao().getAll()
    }

    override fun getNearestTask(): Task? {
        return Task(5, "TestNotificationTitle", "TestNotificationDescription", Color.RED, 30, "31 December", "9:00", 3)
    }

    override fun saveTask(task: Task) {
        database.taskDao().insertAll(task)
    }
}