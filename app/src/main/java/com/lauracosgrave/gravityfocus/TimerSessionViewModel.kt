package com.lauracosgrave.gravityfocus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.lauracosgrave.gravityfocus.data.TimerSession
import com.lauracosgrave.gravityfocus.data.TimerSessionRepository
import kotlinx.coroutines.flow.Flow

class TimerSessionViewModel(val timerSessionRepository: TimerSessionRepository) :
    ViewModel() {

    val timerSessionPagingFlow: Flow<PagingData<TimerSession>> =
        timerSessionRepository.getPagedEvents().cachedIn(viewModelScope)
}