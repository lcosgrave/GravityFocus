package com.lauracosgrave.gravityfocus

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.lauracosgrave.gravityfocus.Timer.UsagePermissionHandler
import com.lauracosgrave.gravityfocus.Timer.UsageStatsHandler
import com.lauracosgrave.gravityfocus.data.TimerSession
import com.lauracosgrave.gravityfocus.data.TimerSessionRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Instant

class TimerViewModel(app: Application) : AndroidViewModel(app) {

    private val _startTimeMins = MutableStateFlow(2)
    val startTimeMins: StateFlow<Int> get() = _startTimeMins
    private val _time = MutableStateFlow(120)
    val time: StateFlow<Int> get() = _time
    private val _timerRunning = MutableStateFlow(false)
    val timerRunning: StateFlow<Boolean> get() = _timerRunning
    private val _disallowedUsageHappened = MutableStateFlow(false)
    val disallowedUsageHappened: StateFlow<Boolean> get() = _disallowedUsageHappened


    private val usageStatsHandler = UsageStatsHandler(app.applicationContext)
    private var timerJob: Job? = null
    private var usageEventsJob: Job? = null

    val permissionHandler = UsagePermissionHandler(app.applicationContext)
    private val _hasUsageStatsPermission = MutableStateFlow(permissionHandler.hasPermission)
    val hasUsageStatsPermission: StateFlow<Boolean> get() = _hasUsageStatsPermission

    private val repository: TimerSessionRepository = (app as GravityFocusApplication).repository

    init {
        _hasUsageStatsPermission.value = permissionHandler.checkHasPermission()
    }

    fun cancelJobs() {
        timerJob?.cancel()
        usageEventsJob?.cancel()
    }

    fun startTimer() {
        cancelJobs()
        _timerRunning.value = true
        val startTimeEpochSecond = Instant.now().epochSecond
        timerJob = viewModelScope.launch {
            delay(1000)
            while (_timerRunning.value && time.value > 0) {
                _time.value--
                delay(1000)
            }
            val endTimeEpochSecond = Instant.now().epochSecond
            repository.insert(
                TimerSession(
                    0L,
                    startTimeEpochSecond,
                    endTimeEpochSecond,
                    durationSeconds = _startTimeMins.value.toLong() * 60
                )
            )
        }
        usageEventsJob = viewModelScope.launch {

            delay(5000)
            while (_timerRunning.value && time.value > 0) {
                Log.d("TimerViewModel", " getting usage stats")
                _disallowedUsageHappened.value = usageStatsHandler.checkForDisallowedUsage(5000)
                if (_disallowedUsageHappened.value) {
                    Log.d("TimerViewModel", " Bad usage detected")

                    stopTimer()

                    val endTimeEpochSecond = Instant.now().epochSecond
                    repository.insert(
                        TimerSession(
                            0L,
                            startTimeEpochSecond,
                            endTimeEpochSecond,
                            completed = false
                        )
                    )
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
        stopTimer()
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

    fun requestUsageStatsPermission(): Intent? {
        return permissionHandler.requestPermission()
    }

    fun onPermissionResult() {
        _hasUsageStatsPermission.value = permissionHandler.checkHasPermission()
    }
}