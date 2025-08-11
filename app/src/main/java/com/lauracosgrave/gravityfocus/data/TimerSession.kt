package com.lauracosgrave.gravityfocus.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TimerSession(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val startTimeFromEpochSeconds: Long,
    val endTimeFromEpochSeconds: Long,
    val completed: Boolean = true,
    val durationSeconds: Long = (endTimeFromEpochSeconds - startTimeFromEpochSeconds),
    val label: String? = null
)
