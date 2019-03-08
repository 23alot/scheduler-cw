package com.boscatov.schedulercw.interactor.predict

import com.boscatov.schedulercw.data.entity.OnCompletePredictEvent
import com.boscatov.schedulercw.data.entity.OnNextPredictEvent
import com.boscatov.schedulercw.data.entity.PredictEvent
import com.boscatov.schedulercw.data.entity.Task
import com.boscatov.schedulercw.data.entity.TaskDate
import com.boscatov.schedulercw.data.repository.neuralnetwork.NeuralNetworkRepository
import com.boscatov.schedulercw.data.repository.scheduler_algorithm.SchedulerAlgorithmRepository
import com.boscatov.schedulercw.data.repository.task.TaskRepository
import io.reactivex.Emitter
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.Date
import javax.inject.Inject

/**
 * Created by boscatov on 08.03.2019.
 */

class PredictInteractorImpl @Inject constructor(
        private val taskRepository: TaskRepository,
        private val neuralNetworkRepository: NeuralNetworkRepository,
        private val schedulerAlgorithmRepository: SchedulerAlgorithmRepository
): PredictInteractor {

    // TODO: Подобрать значения progress
    override fun predict(tasks: List<Task>): Observable<PredictEvent> {
        return Observable.create<PredictEvent> {
            val allTasks = taskRepository.getTasks()
            it.onNext(OnNextPredictEvent(25))
            val predictDates = neuralPredict(tasks, allTasks)
            it.onNext(OnNextPredictEvent(50))
            val resultDates = schedulerAlgorithm(predictDates)
            it.onNext(OnCompletePredictEvent(resultDates))
            it.onComplete()
        }.subscribeOn(Schedulers.io())
    }

    private fun neuralPredict(tasks: List<Task>, allTasks: List<Task>): List<TaskDate> {
        neuralNetworkRepository.fit(allTasks)
        val predictDates = mutableListOf<TaskDate>()
        tasks.forEach {
            val predict = neuralNetworkRepository.predict(it)
            predictDates.add(TaskDate(predict, it.taskDuration))
        }

        return predictDates
    }

    private fun schedulerAlgorithm(predictDates: List<TaskDate>): List<Date> {
        return schedulerAlgorithmRepository.start(predictDates)
    }
}