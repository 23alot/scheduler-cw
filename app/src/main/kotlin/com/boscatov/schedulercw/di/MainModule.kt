package com.boscatov.schedulercw.di

import android.content.Context
import androidx.room.Room
import com.boscatov.schedulercw.data.repository.task.TaskRepository
import com.boscatov.schedulercw.data.repository.task.TaskRepositoryImpl
import com.boscatov.schedulercw.data.source.database.task.TaskDatabase
import com.boscatov.schedulercw.interactor.task.TaskInteractor
import com.boscatov.schedulercw.interactor.task.TaskInteractorImpl
import toothpick.config.Module

class MainModule(context: Context) : Module() {
    init {
        bind(TaskInteractor::class.java).to(TaskInteractorImpl::class.java)
        bind(TaskRepository::class.java).to(TaskRepositoryImpl::class.java)
        val db = Room.databaseBuilder(
            context,
            TaskDatabase::class.java,
            TaskDatabase.TASK_DATABASE_NAME
        ).build()
        bind(TaskDatabase::class.java).toInstance(db)
        bind(Context::class.java).toInstance(context)
    }
}