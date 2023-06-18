package com.example.syncfit.events

import com.example.syncfit.database.entities.Timer
import com.example.syncfit.database.entities.User
import com.example.syncfit.database.models.Interval

sealed interface TimerEvents: AppEvents {
    data class CreateTimer(val timer: Timer): TimerEvents
    data class DeleteTimer(val timer: Timer): TimerEvents
    object DeleteAllTimers: TimerEvents
    data class UpdateTimer(val timer: Timer, val mode: String): TimerEvents
    data class GetTimerByKey(val timer: Timer): TimerEvents
    data class GetTimersByUser(val user: User): TimerEvents
    data class AddTimerInterval(val interval: Interval): TimerEvents
    data class DeleteTimerInterval(val interval: Interval): TimerEvents
    object UpdateTimerIntervals: TimerEvents
    data class GetTimer(val timerId: Int): TimerEvents
}
