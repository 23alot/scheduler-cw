package com.boscatov.schedulercw.data.repository.neuralnetwork

import com.boscatov.schedulercw.data.entity.Task
import java.util.Date

/**
 * Created by boscatov on 08.03.2019.
 */

interface NeuralNetworkRepository {
    /**
     * Заполняет данными
     */
    fun fit(tasks: List<Task>)

    /**
     * Предсказывает Date для Task
     */
    fun predict(task: Task): Date
}