package com.boscatov.schedulercw.di

import com.boscatov.schedulercw.data.repository.neuralnetwork.NeuralNetworkRepository
import com.boscatov.schedulercw.data.repository.neuralnetwork.NeuralNetworkRepositoryImpl
import com.boscatov.schedulercw.data.repository.scheduler_algorithm.SchedulerAlgorithmRepository
import com.boscatov.schedulercw.data.repository.scheduler_algorithm.SchedulerAlgorithmRepositoryImpl
import com.boscatov.schedulercw.interactor.predict.PredictInteractor
import com.boscatov.schedulercw.interactor.predict.PredictInteractorImpl
import toothpick.config.Module

/**
 * Created by boscatov on 08.03.2019.
 */

class PredictModule : Module() {
    init {
        bind(PredictInteractor::class.java)
                .to(PredictInteractorImpl::class.java)
                .singletonInScope()

        bind(NeuralNetworkRepository::class.java)
                .to(NeuralNetworkRepositoryImpl::class.java)
                .singletonInScope()

        bind(SchedulerAlgorithmRepository::class.java)
                .to(SchedulerAlgorithmRepositoryImpl::class.java)
    }
}