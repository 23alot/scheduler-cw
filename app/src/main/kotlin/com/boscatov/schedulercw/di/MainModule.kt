package com.boscatov.schedulercw.di

import com.boscatov.schedulercw.interactor.task.TaskInteractor
import com.boscatov.schedulercw.interactor.task.TaskInteractorImpl
import toothpick.config.Module

class MainModule : Module {
    constructor() {
        bind(TaskInteractor::class.java).to(TaskInteractorImpl::class.java)
    }
}