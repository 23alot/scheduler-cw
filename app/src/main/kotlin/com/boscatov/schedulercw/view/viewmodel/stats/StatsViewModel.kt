package com.boscatov.schedulercw.view.viewmodel.stats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.boscatov.schedulercw.data.entity.Task
import com.boscatov.schedulercw.di.Scopes
import com.boscatov.schedulercw.interactor.project.ProjectInteractor
import com.boscatov.schedulercw.interactor.task.TaskInteractor
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import toothpick.Toothpick
import java.util.*
import javax.inject.Inject

class StatsViewModel : ViewModel() {
    @Inject
    lateinit var taskInteractor: TaskInteractor
    @Inject
    lateinit var projectInteractor: ProjectInteractor
    init {
        val scope = Toothpick.openScope(Scopes.TASK_SCOPE)
        Toothpick.inject(this, scope)
    }
    val tasks: MutableLiveData<List<Task>> = MutableLiveData()

    fun loadTasks() {
        Single.fromCallable {
            val cal = Calendar.getInstance()
            cal.add(Calendar.DAY_OF_YEAR, -7)
            taskInteractor.getTasks(cal.time)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                tasks.value = it
            }, {})
    }

}