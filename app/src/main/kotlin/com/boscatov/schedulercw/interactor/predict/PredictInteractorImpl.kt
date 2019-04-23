package com.boscatov.schedulercw.interactor.predict

import com.boscatov.schedulercw.data.entity.*
import com.boscatov.schedulercw.data.repository.neuralnetwork.NeuralNetworkRepository
import com.boscatov.schedulercw.data.repository.preferences.NNSharedPreferences
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
    private val schedulerAlgorithmRepository: SchedulerAlgorithmRepository,
    private val nnSharedPreferences: NNSharedPreferences
) : PredictInteractor {
    private val values = nnSharedPreferences.getValues()

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
        val predictDates = mutableListOf<Pair<Task, List<Long>>>()
        tasks.forEach {
            val knn = KNN(allTasks, values, 3)
            val taskPredict = knn.predict(it)
            predictDates.add(Pair(it, taskPredict))
        }

        return predictDates
    }

    private fun schedulerAlgorithm(predictDates: List<Pair<Task, List<Long>>>): List<Task> {
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
                        deadline.time,
                        task = pair.first
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
        val resultTasks = mutableListOf<Task>()
        schedulerAlgorithmRepository.schedule(tasks, reserved).forEach { data ->
             data.task?.let {
                 it.taskDateStart = Date(data.resultTime)
                 it.taskDeadLine = null
                 it.taskStatus = TaskStatus.PENDING
                 resultTasks.add(it)
             }
        }
        return resultTasks
    }
}