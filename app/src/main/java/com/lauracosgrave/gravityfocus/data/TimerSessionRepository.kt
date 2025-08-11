package com.lauracosgrave.gravityfocus.data

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

class TimerSessionRepository(private val timerSessionDAO: TimerSessionDAO) {

    suspend fun insert(timerSession: TimerSession) {
        Log.d("TimerSessionRepository", "insert called")
        timerSessionDAO.insert(timerSession)
    }

    suspend fun delete(timerSession: TimerSession) {
        timerSessionDAO.delete(timerSession)
    }


    fun getPagedEvents(): Flow<PagingData<TimerSession>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,                // rows per page
                prefetchDistance = 5,         // load next page when 5 away from end
                enablePlaceholders = false,   // hide “empty slots” if you don’t need them
                initialLoadSize = 40          // first load is 2 pages
            ),
            pagingSourceFactory = { timerSessionDAO.pagingSource() }
        ).flow
    }
}