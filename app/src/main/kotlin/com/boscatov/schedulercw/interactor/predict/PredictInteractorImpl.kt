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
import java.util.*
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

    private fun neuralPredict(tasks: List<Task>, allTasks: List<Task>): List<List<TaskDate>> {
        neuralNetworkRepository.fit(allTasks)
        val now = Calendar.getInstance().time
        val predictDates = mutableListOf<List<TaskDate>>()
        tasks.forEach {
            val calendar = Calendar.getInstance()
            val taskPredict = mutableListOf<TaskDate>()
            for (i in 1..4) {
                it.taskDateStart = calendar.time
                val predict = neuralNetworkRepository.predict(it)
                if (predict > now) {
                    taskPredict.add(TaskDate(predict, it.taskDuration))
                }
                calendar.add(Calendar.DAY_OF_WEEK, 1)
            }
            predictDates.add(taskPredict)
        }

        return predictDates
    }

    private fun schedulerAlgorithm(predictDates: List<List<TaskDate>>): List<Date> {
        return schedulerAlgorithmRepository.start(predictDates)
    }
}