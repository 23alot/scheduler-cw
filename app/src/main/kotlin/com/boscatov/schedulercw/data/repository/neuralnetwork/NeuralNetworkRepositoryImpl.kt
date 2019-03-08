package com.boscatov.schedulercw.data.repository.neuralnetwork

import com.boscatov.schedulercw.data.entity.Task
import java.util.Date

/**
 * Created by boscatov on 08.03.2019.
 */

class NeuralNetworkRepositoryImpl : NeuralNetworkRepository {
    override fun fit(tasks: List<Task>) {

    }

    override fun predict(task: Task): Date {
        return Date()
    }
}