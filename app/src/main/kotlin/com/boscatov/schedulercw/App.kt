package com.boscatov.schedulercw

import android.app.Application
import com.boscatov.schedulercw.di.ToothpickInitializer

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        ToothpickInitializer.initialize(this)
    }
}