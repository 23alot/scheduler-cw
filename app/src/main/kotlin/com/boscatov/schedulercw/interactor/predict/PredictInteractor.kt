package com.boscatov.schedulercw.interactor.predict

import com.boscatov.schedulercw.data.entity.PredictEvent
import com.boscatov.schedulercw.data.entity.Task
import io.reactivex.Observable
import java.util.*

interface PredictInteractor {
    /**
     * Возвращает список времени для входящих задач
     */
    fun predict(tasks: List<Task>): Observable<PredictEvent>
}