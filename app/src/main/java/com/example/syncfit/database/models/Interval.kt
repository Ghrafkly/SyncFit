package com.example.syncfit.database.models

import kotlinx.serialization.Serializable

@Serializable
data class Interval(
    val intervalId: Int,
    val intervalName: String,
    val intervalDuration: Duration,
) {
    companion object {
        private var lastId = 0
    }

    constructor(intervalName: String, intervalDuration: Duration) : this(
        intervalId = ++lastId,
        intervalName = intervalName,
        intervalDuration = intervalDuration,
    )

    constructor() : this(0,"", Duration())
}
