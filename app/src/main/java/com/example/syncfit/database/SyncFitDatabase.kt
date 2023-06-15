package com.example.syncfit.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.syncfit.database.daos.TimerDao
import com.example.syncfit.database.daos.UserDao
import com.example.syncfit.database.entities.Timer
import com.example.syncfit.database.entities.User

@Database(
    entities = [User::class, Timer::class],
    version = 1,
)
@TypeConverters(Converters::class)
abstract class SyncFitDatabase: RoomDatabase() {
    abstract val userDao: UserDao
    abstract val timerDao: TimerDao
}
