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
import java.util.Date
import javax.inject.Inject

class TaskListViewModel : ViewModel() {

    val day = MutableLiveData<Date>()
    val tasks = MutableLiveData<List<Task>>()

    @Inject
    lateinit var taskInteractor: TaskInteractor
    init {
        val scope = Toothpick.openScope(Scopes.TASK_SCOPE)
        Toothpick.inject(this, scope)
        day.value = Calendar.getInstance().time
    }

    fun loadData() {
        Observable.fromCallable {
            taskInteractor.getDateTasks(day.value!!)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
            tasks.value = it
        }
    }

    fun increaseDate() {
        val calendar = Calendar.getInstance()
        calendar.time = day.value
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        day.value = calendar.time
    }

    fun decreaseDate() {
        val calendar = Calendar.getInstance()
        calendar.time = day.value
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        day.value = calendar.time
    }
}