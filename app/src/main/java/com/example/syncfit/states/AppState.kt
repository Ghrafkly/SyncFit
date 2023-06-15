package com.example.syncfit.states


data class AppState(
    val userState: UserState = UserState(),
    val timerState: TimerState = TimerState(),
)
