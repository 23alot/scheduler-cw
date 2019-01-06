package com.boscatov.schedulercw.view.viewmodel.task_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.boscatov.schedulercw.data.entity.Task
import com.boscatov.schedulercw.di.Scopes
import com.boscatov.schedulercw.interactor.task.TaskInteractor
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import toothpick.Toothpick
import java.util.Calendar
import javax.inject.Inject

class TaskListViewModel : ViewModel() {


    val tasks = MutableLiveData<List<Task>>()

    @Inject
    lateinit var taskInteractor: TaskInteractor
    init {
        val scope = Toothpick.openScope(Scopes.TASK_SCOPE)
        Toothpick.inject(this, scope)
    }

    fun loadData() {
        Observable.fromCallable {
            val today = Calendar.getInstance().time
            taskInteractor.getDateTasks(today)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
            tasks.value = it
        }
    }
}