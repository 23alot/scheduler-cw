package com.boscatov.schedulercw.view.viewmodel.chaos

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.boscatov.schedulercw.data.entity.OnCompletePredictEvent
import com.boscatov.schedulercw.data.entity.Task
import com.boscatov.schedulercw.data.entity.TaskStatus
import com.boscatov.schedulercw.di.Scopes
import com.boscatov.schedulercw.interactor.predict.PredictInteractor
import com.boscatov.schedulercw.interactor.task.TaskInteractor
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import toothpick.Toothpick
import java.util.*
import javax.inject.Inject

class ChaosViewModel : ViewModel() {
    @Inject
    lateinit var taskInteractor: TaskInteractor

    @Inject
    lateinit var predictInteractor: PredictInteractor

    init {
        val scope = Toothpick.openScope(Scopes.TASK_SCOPE)
        Toothpick.inject(this, scope)
    }
    val tasks = MutableLiveData<List<Task>>()

    fun onLoadTasks() {
        val disposable = taskInteractor.getTasks(arrayOf(TaskStatus.ABANDONED, TaskStatus.WAIT_FOR_ACTION))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                tasks.value = it
            }
    }

    fun onPredictTasks() {
        tasks.value?.let {
            predictInteractor.predict(it).observeOn(AndroidSchedulers.mainThread()).subscribe { event ->
                when (event) {
                    is OnCompletePredictEvent -> predictComplete(event.predictDates)
                }
            }
        }
    }

    private fun predictComplete(tasks: List<Task>) {
        Completable.fromAction {
            for(task in tasks) {
                Log.d("ChaosViewModel", "$task")
                taskInteractor.updateTask(task)
            }
        }.subscribeOn(Schedulers.computation()).subscribe {
            onLoadTasks()
        }
    }
}