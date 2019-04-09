package com.boscatov.schedulercw.view.viewmodel.holder

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.boscatov.schedulercw.view.ui.state.DefaultState
import com.boscatov.schedulercw.view.ui.state.NewTaskAcceptState
import com.boscatov.schedulercw.view.ui.state.NewTaskCreateCompleteState
import com.boscatov.schedulercw.view.ui.state.NewTaskState
import com.boscatov.schedulercw.view.ui.state.State
import com.boscatov.schedulercw.worker.NearestTaskWorker
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MainViewModel : ViewModel() {
    companion object {
        private const val TAG = "MainViewModel"
        const val TASK_WORKER_TAG = "Monitor notification worker"
    }
    var state = MutableLiveData<State>()

    fun initNotificationWorker() {
        Observable.range(1, 5)
            .concatMap<Any> { i -> Observable.just(i).delay(3, TimeUnit.MINUTES) }
            .doOnNext { i ->
                val nearestTaskBuilder = PeriodicWorkRequestBuilder<NearestTaskWorker>(15, TimeUnit.MINUTES)
                Log.d(TAG, "${TASK_WORKER_TAG}_$i ${System.currentTimeMillis()/1000}")
                nearestTaskBuilder.addTag("${TASK_WORKER_TAG}_$i")
                val nearestTask = nearestTaskBuilder.build()
                WorkManager.getInstance().enqueueUniquePeriodicWork("${TASK_WORKER_TAG}_$i", ExistingPeriodicWorkPolicy.REPLACE, nearestTask)
            }.doOnComplete { }.observeOn(Schedulers.io()).subscribe()
        val nearestTaskBuilder = PeriodicWorkRequestBuilder<NearestTaskWorker>(15, TimeUnit.MINUTES)
        nearestTaskBuilder.addTag("${TASK_WORKER_TAG}_0")
        val nearestTask = nearestTaskBuilder.build()
        WorkManager.getInstance().enqueueUniquePeriodicWork("${TASK_WORKER_TAG}_0", ExistingPeriodicWorkPolicy.REPLACE, nearestTask)
    }

    fun testNetwork() {

    }

    fun onOpenNewTaskDialog() {
        state.value = NewTaskState()
    }

    fun onCloseNewTaskDialog() {
        state.value = DefaultState()
    }

    fun onAcceptNewTask() {
        state.value = NewTaskAcceptState()
    }

    fun onNewTaskComplete() {
        state.value = DefaultState()
        val nearestTaskBuilder = OneTimeWorkRequestBuilder<NearestTaskWorker>()
        WorkManager.getInstance().enqueue(nearestTaskBuilder.build())
    }
}