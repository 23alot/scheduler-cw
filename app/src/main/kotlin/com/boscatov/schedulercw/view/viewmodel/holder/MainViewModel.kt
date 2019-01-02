package com.boscatov.schedulercw.view.viewmodel.holder

import androidx.lifecycle.ViewModel
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.boscatov.schedulercw.worker.NearestTaskWorker
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MainViewModel : ViewModel() {
    fun initNotificationWorker() {
        Observable.range(1, 5)
            .concatMap<Any> { i -> Observable.just(i).delay(3, TimeUnit.MINUTES) }
            .doOnNext { i ->
                val nearestTaskBuilder = PeriodicWorkRequestBuilder<NearestTaskWorker>(15, TimeUnit.MINUTES)
                nearestTaskBuilder.addTag(TASK_WORKER_TAG)
                val nearestTask = nearestTaskBuilder.build()
                WorkManager.getInstance().enqueue(nearestTask)
            }.doOnComplete { }.observeOn(Schedulers.io()).subscribe()
    }

    companion object {
        const val TASK_WORKER_TAG = "Monitor notification worker"
    }
}