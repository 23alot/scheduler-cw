package com.boscatov.schedulercw.data.source.database.project

import androidx.room.Database
import androidx.room.RoomDatabase
import com.boscatov.schedulercw.data.entity.Project
import com.boscatov.schedulercw.data.source.database.task.TaskDao

/**
 * Created by boscatov on 13.04.2019.
 */



@Database(entities = [Project::class], version = 1)
abstract class ProjectDatabase : RoomDatabase() {
    companion object {
        const val PROJECT_DATABASE_NAME = "PROJECT_DATABASE"
    }

    abstract fun projectDatabase(): TaskDao
}