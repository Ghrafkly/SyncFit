package com.example.syncfit.states

import com.example.syncfit.database.entities.Timer
import com.example.syncfit.database.entities.UserWithTimers

data class TimerState(
    val timers: List<UserWithTimers> = emptyList(),
    val timer: Timer = Timer(),
    val isTimerAdded: Boolean = false,
    val isTimerUpdated: Boolean = false,
    val isTimerDeleted: Boolean = false,
    val timerError: String? = null,
)
