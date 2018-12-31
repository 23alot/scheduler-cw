package com.boscatov.schedulercw.di

import com.boscatov.schedulercw.data.repository.task.TaskRepository
import com.boscatov.schedulercw.data.repository.task.TaskRepositoryImpl
import com.boscatov.schedulercw.interactor.task.TaskInteractor
import com.boscatov.schedulercw.interactor.task.TaskInteractorImpl
import toothpick.config.Module

class MainModule : Module() {
    init {
        bind(TaskInteractor::class.java).to(TaskInteractorImpl::class.java)
        bind(TaskRepository::class.java).to(TaskRepositoryImpl::class.java)
    }
}