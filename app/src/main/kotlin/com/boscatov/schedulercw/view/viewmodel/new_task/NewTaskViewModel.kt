package com.boscatov.schedulercw.view.viewmodel.new_task

import android.graphics.Color
import androidx.lifecycle.ViewModel
import com.boscatov.schedulercw.data.entity.Task
import com.boscatov.schedulercw.di.Scopes
import com.boscatov.schedulercw.interactor.task.TaskInteractor
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import toothpick.Toothpick
import javax.inject.Inject

class NewTaskViewModel : ViewModel() {
    @Inject
    lateinit var taskInteractor: TaskInteractor
    init {
        val scope = Toothpick.openScope(Scopes.TASK_SCOPE)
        Toothpick.inject(this, scope)
    }

    fun onAcceptNewTask() {
        Observable.fromCallable {
            val task = Task(taskTitle = "TestBDTitle", taskDescription = "TestBDDescription", taskPriority = 2, taskTimeStart = "Sep 1", taskColor = Color.RED, taskDuration = 30, taskDateStart = "Sep 2")
            taskInteractor.saveTask(task)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
        }

    }
}