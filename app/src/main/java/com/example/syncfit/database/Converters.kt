package com.example.syncfit.database

import androidx.room.TypeConverter
import com.example.syncfit.database.models.Duration
import com.example.syncfit.database.models.Interval
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun fromDuration(duration: Duration): String {
        return Json.encodeToString(Duration.serializer(), duration)
    }

    @TypeConverter
    fun toDuration(duration: String): Duration {
        return Json.decodeFromString(Duration.serializer(), duration)
    }

    @TypeConverter
    fun fromIntervalList(intervals: List<Interval>): String {
        return Json.encodeToString(ListSerializer(Interval.serializer()), intervals)
    }

    @TypeConverter
    fun toIntervalList(intervals: String): List<Interval> {
        return Json.decodeFromString(ListSerializer(Interval.serializer()), intervals)
    }
}
