package com.boscatov.schedulercw.data.source.database.task

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.boscatov.schedulercw.data.entity.Task

@Database(entities = [Task::class], version = 2)
abstract class TaskDatabase : RoomDatabase() {
    companion object {
        const val TASK_DATABASE_NAME = "TASK_DATABASE"
        val migration1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE TASK ADD COLUMN task_is_done INTEGER DEFAULT 0 NOT NULL")
            }
        }
    }

    abstract fun taskDao(): TaskDao
}