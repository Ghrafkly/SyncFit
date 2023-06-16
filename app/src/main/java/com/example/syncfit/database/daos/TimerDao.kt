package com.example.syncfit.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.example.syncfit.database.entities.Timer
import com.example.syncfit.database.entities.UserWithTimers
import kotlinx.coroutines.flow.Flow

@Dao
interface TimerDao {
    @Upsert
    suspend fun upsertTimer(timer: Timer)

    @Transaction
    @Query("SELECT * FROM users WHERE email = :key")
    fun getTimersByUser(key: String): Flow<List<UserWithTimers>>

    @Query("SELECT * FROM timers WHERE timerId = :key")
    fun getTimerByKey(key: Int): Flow<Timer>
}
