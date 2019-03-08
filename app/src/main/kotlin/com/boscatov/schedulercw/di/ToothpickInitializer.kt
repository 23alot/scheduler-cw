package com.boscatov.schedulercw.di

import android.content.Context
import toothpick.Toothpick

object ToothpickInitializer {
    fun initialize(context: Context) {
        Toothpick.openScope(Scopes.TASK_SCOPE).installModules(MainModule(context), PredictModule())
    }
}