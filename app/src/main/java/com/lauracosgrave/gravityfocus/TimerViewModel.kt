package com.lauracosgrave.gravityfocus

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class TimerViewModel(context: Context) : ViewModel() {
    private val _startTimeMins = MutableStateFlow(2)
    val startTimeMins: StateFlow<Int> get() = _startTimeMins
    private val _time = MutableStateFlow(120)
    val time: StateFlow<Int> get() = _time
    private val _timerRunning = MutableStateFlow(false)
    val timerRunning: StateFlow<Boolean> get() = _timerRunning
    private val _disallowedUsageHappened = MutableStateFlow(false)
    val disallowedUsageHappened: StateFlow<Boolean> get() = _disallowedUsageHappened

    private val usageStatsHandler = UsageStatsHandler(context)
    private var timerJob: Job? = null
    private var usageEventsJob: Job? = null

    fun cancelJobs() {
        timerJob?.cancel()
        usageEventsJob?.cancel()
    }

    fun startTimer() {
        cancelJobs()
        _timerRunning.value = true
        timerJob = viewModelScope.launch {
            delay(1000)
            while (_timerRunning.value && time.value > 0) {
                _time.value--
                delay(1000)
            }
        }
        viewModelScope.launch {

            delay(5000)
            while (_timerRunning.value && time.value > 0) {
                Log.d("TimerViewModel", " getting usage stats")
                _disallowedUsageHappened.value = usageStatsHandler.checkForDisallowedUsage(5000)
                if (_disallowedUsageHappened.value) {
                    Log.d("TimerViewModel", " Bad usage detected")
                    stopTimer()
                }
                delay(5000)
            }
        }

        Log.d("TimerViewModel", " starting timer")
    }


    fun stopTimer() {
        _timerRunning.value = false
        cancelJobs()
        Log.d("TimerViewModel", "stopping timer")
    }

    fun startOrStopTimer() {
        if (_timerRunning.value) {
            stopTimer()
        } else {
            startTimer()
        }
        Log.d("TimerViewModel", "stopping or starting timer")
    }

    fun resetTimer() {
        cancelJobs()
        _timerRunning.value = false
        _time.value = _startTimeMins.value * 60
        _disallowedUsageHappened.value = false
        Log.d("TimerViewModel", "reset timer")
    }

    fun updateStartTime(newStartTimeMins: Int) {
        stopTimer()
        Log.d("TimerViewModel", "updating")
        _time.value = newStartTimeMins * 60
        _startTimeMins.value = newStartTimeMins
        resetTimer()
        Log.d("TimerViewModel", "update start time")

    }
}
