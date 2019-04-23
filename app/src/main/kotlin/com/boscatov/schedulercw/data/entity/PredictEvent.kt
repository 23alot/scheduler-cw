package com.boscatov.schedulercw.data.entity

import java.util.Date

/**
 * Created by boscatov on 08.03.2019.
 */

sealed class PredictEvent

class OnNextPredictEvent(val progress: Int): PredictEvent()

class OnCompletePredictEvent(val predictDates: List<Task>): PredictEvent()