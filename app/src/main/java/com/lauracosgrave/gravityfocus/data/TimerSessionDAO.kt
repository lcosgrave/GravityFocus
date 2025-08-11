package com.lauracosgrave.gravityfocus.data

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TimerSessionDAO {
    @Insert
    suspend fun insert(timerSession: TimerSession)

    @Delete
    suspend fun delete(timerSession: TimerSession)

    @Query("SELECT * FROM TimerSession ORDER BY startTimeFromEpochSeconds DESC")
    fun pagingSource(): PagingSource<Int, TimerSession>

    @Query("SELECT * FROM TimerSession WHERE startTimeFromEpochSeconds > :timeEpochSeconds ")
    fun getAllAfterTimestamp(timeEpochSeconds: Long): Flow<List<TimerSession>>

    @Query("SELECT * FROM TimerSession WHERE startTimeFromEpochSeconds > :startTimeEpochSeconds AND startTimeFromEpochSeconds < :endTimeEpochSeconds")
    fun getAllBetweenTimestamps(
        startTimeEpochSeconds: Long,
        endTimeEpochSeconds: Long
    ): Flow<List<TimerSession>>


}