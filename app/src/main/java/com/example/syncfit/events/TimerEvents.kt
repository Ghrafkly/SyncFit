package com.example.syncfit.events

import com.example.syncfit.database.entities.Timer
import com.example.syncfit.database.entities.User
import com.example.syncfit.database.models.Environment
import com.example.syncfit.database.models.Intensity
import com.example.syncfit.database.models.Interval

sealed interface TimerEvents: AppEvents {
    data class CreateTimer(val timer: Timer): TimerEvents
    data class DeleteTimer(val timer: Timer): TimerEvents
    object UpdateTimer: TimerEvents
    data class GetTimerByKey(val timer: Timer): TimerEvents
    data class GetTimersByUser(val user: User): TimerEvents

    data class UpdateTimerName(val name: String): TimerEvents
    data class UpdateTimerIntervals(val intervals: List<Interval>): TimerEvents
    data class UpdateTimerRepeats(val repeats: Int): TimerEvents
    data class UpdateTimerIntensity(val intensity: Intensity): TimerEvents
    data class UpdateTimerEnvironment(val environment: Environment): TimerEvents
}
