package com.boscatov.schedulercw.interactor.scheduler

import io.reactivex.Completable

interface SchedulerInteractor {
    /**
     * Получаем задачу, меняем статус на Active, меняем время начала на текущее, обновляем задачу в бд
     *
     */
    fun startTaskCompletable(taskId:Long) : Completable

    /**
     * Получаем задачу, меняем статус на {@link TaskStatus.DONE}
     */
    fun completeTaskCompletable(taskId: Long) : Completable

    /**
     * Получаем задачу, меняем статус на {@link TaskStatus.WAIT_FOR_ACTION}
     */
    fun notifyTaskShouldBeEndedCompletable(taskId: Long) : Completable

    /**
     * Получаем задачу, меняем статус на {@link TaskStatus.ABANDONED}
     */
    fun abandonTaskCompletable(taskId: Long) : Completable
}