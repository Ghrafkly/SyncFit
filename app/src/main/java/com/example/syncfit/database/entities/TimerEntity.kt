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
    @PrimaryKey(autoGenerate = true) val timerId: Int,
    val userId: String,
    var timerName: String,
    var timerIntervals: List<Interval>,
    var timerDuration: Duration,
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
        timerIntervals = emptyList(),
        timerDuration = Duration(),
        timerRepeats = 0,
        timerIntensity = Intensity.NONE,
        timerEnvironment = Environment.NONE,
        timerDateCreated = 0,
        timerDateLastUsed = 0,
        timerTimesUsed = 0,
    )
}
