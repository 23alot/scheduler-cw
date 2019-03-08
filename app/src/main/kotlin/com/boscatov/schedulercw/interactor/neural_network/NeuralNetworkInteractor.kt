package com.boscatov.schedulercw.interactor.neural_network

import com.boscatov.schedulercw.data.entity.Task
import java.util.*

interface NeuralNetworkInteractor {
    /**
     * Возвращает список возможного времени с вероятностью по задаче
     */
    fun predict(task: Task): Date
}