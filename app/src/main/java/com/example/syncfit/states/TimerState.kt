package com.example.syncfit.states

import com.example.syncfit.database.entities.Timer
import com.example.syncfit.database.entities.UserWithTimers

data class TimerState(
    val timer: Timer = Timer(),
    val timers: List<Timer> = emptyList(),
    val isTimerRunning: Boolean = false,
    val isTimerCreateSuccessful: Boolean = false,
    val timerError: String? = null,

    var isTimerNameValid: Boolean = true,
    var isTimerIntervalsValid: Boolean = true,
    var isTimerIntensityValid: Boolean = true,
    var isTimerEnvironmentValid: Boolean = true,
    var isTimerRepeatsValid: Boolean = true,
)
