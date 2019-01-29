package com.boscatov.schedulercw.view.viewmodel.edit_task

import androidx.lifecycle.ViewModel
import com.boscatov.schedulercw.data.entity.Task
import com.boscatov.schedulercw.di.Scopes
import com.boscatov.schedulercw.interactor.task.TaskInteractor
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import toothpick.Toothpick
import javax.inject.Inject

class EditTaskViewModel : ViewModel() {
    @Inject
    lateinit var taskInteractor: TaskInteractor
    init {
        val scope = Toothpick.openScope(Scopes.TASK_SCOPE)
        Toothpick.inject(this, scope)
    }

    fun onAcceptNewTask(task: Task) {
        Observable.fromCallable {
            taskInteractor.saveTask(task)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
        }

    }
}