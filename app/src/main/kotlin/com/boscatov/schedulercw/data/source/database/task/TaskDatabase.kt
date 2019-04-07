package com.boscatov.schedulercw.data.source.database.task

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.boscatov.schedulercw.data.entity.Task

@Database(entities = [Task::class], version = 5)
@TypeConverters(Converters::class)
abstract class TaskDatabase : RoomDatabase() {
    companion object {
        const val TASK_DATABASE_NAME = "TASK_DATABASE"
    }

    abstract fun taskDao(): TaskDao
}