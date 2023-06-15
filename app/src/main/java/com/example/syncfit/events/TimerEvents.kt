package com.example.syncfit.events

import com.example.syncfit.database.entities.Timer
import com.example.syncfit.database.entities.User

sealed interface TimerEvents: AppEvents {
    data class CreateTimer(val timer: Timer): TimerEvents
    data class DeleteTimer(val timer: Timer): TimerEvents
    data class UpdateTimer(val timer: Timer): TimerEvents
    data class GetTimerByKey(val timer: Timer): TimerEvents
    data class GetTimersByUser(val user: User): TimerEvents
}
