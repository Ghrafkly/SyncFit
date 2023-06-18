package com.example.syncfit.database.models

import kotlinx.serialization.Serializable

@Serializable
data class Interval(
    var intervalId: Int = 0,
    val intervalName: String = "",
    val intervalTimeStamp: Long = 0L,
)
