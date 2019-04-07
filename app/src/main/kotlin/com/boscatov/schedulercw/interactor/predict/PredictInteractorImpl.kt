package com.boscatov.schedulercw.interactor.predict

import com.boscatov.schedulercw.data.entity.*
import com.boscatov.schedulercw.data.repository.neuralnetwork.NeuralNetworkRepository
import com.boscatov.schedulercw.data.repository.scheduler_algorithm.ReservedData
import com.boscatov.schedulercw.data.repository.scheduler_algorithm.SchedulerAlgorithmRepository
import com.boscatov.schedulercw.data.repository.scheduler_algorithm.SchedulerData
import com.boscatov.schedulercw.data.repository.task.TaskRepository
import com.boscatov.schedulercw.data.source.custom.networks.KNN
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
) : PredictInteractor {

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

    private fun neuralPredict(tasks: List<Task>, allTasks: List<Task>): List<Pair<Task, List<Long>>> {
//        neuralNetworkRepository.fit(allTasks)
        val now = Calendar.getInstance().time
        val predictDates = mutableListOf<Pair<Task, List<Long>>>()
        tasks.forEach {
            val calendar = Calendar.getInstance()
            val knn = KNN(allTasks, listOf(20.0, 15.0), 3)
            val taskPredict = knn.predict(it)
            predictDates.add(Pair(it, taskPredict))
        }

        return predictDates
    }

    private fun schedulerAlgorithm(predictDates: List<Pair<Task, List<Long>>>): List<Date> {
        val date = Calendar.getInstance().time
        val existTasks = taskRepository.getTasks(date)
        val tasks: MutableList<SchedulerData> = mutableListOf()
        val reserved: MutableList<ReservedData> = mutableListOf()
        predictDates.forEach { pair ->
            pair.first.taskDeadLine?.let { deadline ->
                tasks.add(
                    SchedulerData(
                        pair.first.taskPriority,
                        pair.first.taskDuration * 60 * 1000L,
                        pair.second,
                        deadline.time
                    )
                )
            }
        }
        existTasks.forEach {
            it.taskDateStart?.let {date ->
                reserved.add(
                    ReservedData(
                        date.time,
                        date.time + it.taskDuration * 60 * 1000L
                    )
                )
            }
        }
        return schedulerAlgorithmRepository.schedule(tasks, reserved).map { Date(it.resultTime) }
    }
}