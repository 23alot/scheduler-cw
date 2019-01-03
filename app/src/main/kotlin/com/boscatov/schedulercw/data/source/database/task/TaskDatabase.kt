package com.boscatov.schedulercw.data.source.database.task

import androidx.room.Database
import androidx.room.RoomDatabase
import com.boscatov.schedulercw.data.entity.Task

@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {
    companion object {
        const val TASK_DATABASE_NAME = "TASK_DATABASE"
    }
    abstract fun taskDao(): TaskDao
}