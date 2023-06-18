package com.example.syncfit.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.syncfit.database.models.Duration
import com.example.syncfit.database.models.Environment
import com.example.syncfit.database.models.Intensity
import com.example.syncfit.database.models.Interval

@Entity(
    tableName = "timers",
    indices = [
        Index(
            value = ["userId", "timerId"],
            unique = true
        )
    ],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["email"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE,
        )
    ]
)
data class Timer(
    val userId: String,
    @PrimaryKey(autoGenerate = true) val timerId: Int,
    var timerName: String,
    var timerIntervals: List<Interval>,
    var timerTimeStamp: Long,
    var timerRepeats: Int,
    var timerIntensity: Intensity,
    var timerEnvironment: Environment,
    val timerDateCreated: Long,
    var timerDateLastUsed: Long,
    var timerTimesUsed: Int,
) {
    constructor() : this(
        timerId = 0,
        userId = "",
        timerName = "",
        timerIntervals = listOf(),
        timerTimeStamp = 0L,
        timerRepeats = 0,
        timerIntensity = Intensity.NONE,
        timerEnvironment = Environment.NONE,
        timerDateCreated = System.currentTimeMillis(),
        timerDateLastUsed = System.currentTimeMillis(),
        timerTimesUsed = 0,
    )

    constructor(
        userId: String,
        timerName: String,
        timerIntervals: List<Interval>,
        timerTimeStamp: Long,
        timerRepeats: Int,
        timerIntensity: Intensity,
        timerEnvironment: Environment,
        timerDateLastUsed: Long,
    ) : this(
        timerId = 0,
        userId = userId,
        timerName = timerName,
        timerIntervals = timerIntervals,
        timerTimeStamp = timerTimeStamp,
        timerRepeats = timerRepeats,
        timerIntensity = timerIntensity,
        timerEnvironment = timerEnvironment,
        timerDateCreated = System.currentTimeMillis(),
        timerDateLastUsed = timerDateLastUsed,
        timerTimesUsed = 0,
    )
}
