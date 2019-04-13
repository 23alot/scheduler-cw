package com.boscatov.schedulercw.view.viewmodel.task_list

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.boscatov.schedulercw.data.entity.Task
import com.boscatov.schedulercw.data.entity.TaskStatus
import com.boscatov.schedulercw.di.Scopes
import com.boscatov.schedulercw.interactor.scheduler.SchedulerInteractor
import com.boscatov.schedulercw.interactor.task.TaskInteractor
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import toothpick.Toothpick
import java.util.*
import javax.inject.Inject

class TaskListViewModel : ViewModel() {

    val day = MutableLiveData<Date>()
    val tasks = MutableLiveData<List<Task>>()

    @Inject
    lateinit var taskInteractor: TaskInteractor

    @Inject
    lateinit var schedulerInteractor: SchedulerInteractor

    init {
        val scope = Toothpick.openScope(Scopes.TASK_SCOPE)
        Toothpick.inject(this, scope)
        day.value = Calendar.getInstance().time
    }

    fun loadData() {
        // TODO: Обработать Disposable
        Observable.fromCallable {
            Log.d("123", "load request")
            taskInteractor.getDateTasks(day.value!!)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { taskList ->
                Log.d("123", "load complete $taskList")
                val result = taskList.filter { it.taskStatus == TaskStatus.ACTIVE || it.taskStatus == TaskStatus.DONE || it.taskStatus == TaskStatus.PENDING }
                Log.d("123", "load complete2 $result")
                tasks.value = result
            }
    }

    fun getCurrentTaskId(): Int? {
        val now = Calendar.getInstance().time
        val id = 0
        tasks.value?.let {
            for ((i, task) in it.withIndex()) {
                task.taskDateStart?.let { date ->
                    if (date > now) {
                        return i
                    }
                }
            }
        }
        return id
    }

    fun onChaosSwipe(position: Int) {
        val task = tasks.value!![position]
        schedulerInteractor.abandonTaskCompletable(task.taskId).subscribe()
        loadData()
    }

    fun onDoneSwipe(position: Int) {
        val task = tasks.value!![position]
        schedulerInteractor.completeTaskCompletable(task.taskId).subscribe()
        loadData()
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