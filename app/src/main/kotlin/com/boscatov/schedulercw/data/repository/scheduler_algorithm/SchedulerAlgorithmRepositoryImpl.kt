package com.boscatov.schedulercw.data.repository.scheduler_algorithm

import com.boscatov.schedulercw.data.entity.TaskDate
import java.util.Date
import javax.inject.Inject

/**
 * Created by boscatov on 08.03.2019.
 */
class SchedulerAlgorithmRepositoryImpl @Inject constructor(): SchedulerAlgorithmRepository {
    override fun start(taskDates: List<List<TaskDate>>): List<Date> {
        return listOf()
    }
}