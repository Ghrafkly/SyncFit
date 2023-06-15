package com.example.syncfit.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.syncfit.database.entities.Timer
import com.example.syncfit.database.entities.UserWithTimers
import kotlinx.coroutines.flow.Flow

@Dao
interface TimerDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createTimer(timer: Timer)

    @Delete
    suspend fun deleteTimer(timer: Timer)
    @Update
    suspend fun updateTimer(timer: Timer)

    @Transaction
    @Query("SELECT * FROM users WHERE email = :key")
    fun getTimersByUser(key: String): Flow<List<UserWithTimers>>

    @Query("SELECT * FROM timers WHERE timerId = :key")
    fun getTimerByKey(key: Int): Flow<Timer>
}
