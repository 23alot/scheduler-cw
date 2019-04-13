package com.boscatov.schedulercw.di

import android.content.Context
import androidx.room.Room
import com.boscatov.schedulercw.data.repository.project.ProjectRepository
import com.boscatov.schedulercw.data.repository.project.ProjectRepositoryImpl
import com.boscatov.schedulercw.data.repository.task.TaskRepository
import com.boscatov.schedulercw.data.repository.task.TaskRepositoryImpl
import com.boscatov.schedulercw.data.source.database.project.ProjectDatabase
import com.boscatov.schedulercw.data.source.database.task.TaskDatabase
import com.boscatov.schedulercw.interactor.project.ProjectInteractor
import com.boscatov.schedulercw.interactor.project.ProjectInteractorImpl
import com.boscatov.schedulercw.interactor.scheduler.SchedulerInteractor
import com.boscatov.schedulercw.interactor.scheduler.SchedulerInteractorImpl
import com.boscatov.schedulercw.interactor.task.TaskInteractor
import com.boscatov.schedulercw.interactor.task.TaskInteractorImpl
import toothpick.config.Module

class MainModule(context: Context) : Module() {
    init {
        bind(TaskInteractor::class.java).to(TaskInteractorImpl::class.java).singletonInScope()
        bind(TaskRepository::class.java).to(TaskRepositoryImpl::class.java).singletonInScope()
        bind(ProjectInteractor::class.java).to(ProjectInteractorImpl::class.java).singletonInScope()
        bind(ProjectRepository::class.java).to(ProjectRepositoryImpl::class.java).singletonInScope()
        bind(SchedulerInteractor::class.java).to(SchedulerInteractorImpl::class.java).singletonInScope()
        val db = Room.databaseBuilder(
            context,
            TaskDatabase::class.java,
            TaskDatabase.TASK_DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
        bind(TaskDatabase::class.java).toInstance(db)
        val projectDb = Room.databaseBuilder(
            context,
            ProjectDatabase::class.java,
            ProjectDatabase.PROJECT_DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
        bind(ProjectDatabase::class.java).toInstance(projectDb)
        bind(Context::class.java).toInstance(context)
    }
}