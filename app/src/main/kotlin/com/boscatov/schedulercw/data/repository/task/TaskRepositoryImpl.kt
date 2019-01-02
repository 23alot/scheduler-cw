package com.boscatov.schedulercw.data.repository.task

import android.graphics.Color
import com.boscatov.schedulercw.data.entity.Task
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor() : TaskRepository {
    override fun getTasks(): List<Task> {
        val test = arrayListOf<Task>()
        val colors = listOf(Color.RED, Color.BLACK, Color.BLUE, Color.GREEN, Color.YELLOW, Color.GRAY)
        for (i in 0..5) {
            val task = Task(
                i.toLong(),
                taskTitle = "TestTitle$i",
                taskDescription = "TestDescription$i",
                taskDateStart = "TestDateStart$i",
                taskDuration = i,
                taskColor = colors[i],
                taskTimeStart = "${i+5}:00"
            )
            test.add(task)
        }
        return test
    }

    override fun getNearestTask(): Task? {
        return Task(5, "TestNotificationTitle", "TestNotificationDescription", Color.RED, 30, "31 December", "9:00")
    }
}