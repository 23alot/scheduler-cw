package com.boscatov.schedulercw.data.repository.scheduler_algorithm

import com.boscatov.schedulercw.data.entity.TaskDate
import java.util.Date

/**
 * Created by boscatov on 08.03.2019.
 */

interface SchedulerAlgorithmRepository {
    fun schedule(tasks: List<SchedulerData>, reserved: List<ReservedData>): List<SchedulerData>
}