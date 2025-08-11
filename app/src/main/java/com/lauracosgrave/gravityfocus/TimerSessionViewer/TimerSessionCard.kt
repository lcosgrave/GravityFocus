package com.lauracosgrave.gravityfocus.TimerSessionViewer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lauracosgrave.gravityfocus.Timer.displayTime
import com.lauracosgrave.gravityfocus.data.TimerSession
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Composable
fun TimerSessionCard(timerSession: TimerSession?) {
    if (timerSession != null) {
        Card(
            modifier = Modifier.padding(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (timerSession.completed) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer,
            ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Start time: ${convertEpochSecondsToDateTime(timerSession.startTimeFromEpochSeconds)}",
                    modifier = Modifier.padding(8.dp)
                )
                Text(
                    "Duration: ${displayTime(timerSession.durationSeconds.toInt())}",
                    modifier = Modifier.padding(8.dp)
                )

            }
        }
    }
}

fun convertEpochSecondsToDateTime(epochSeconds: Long): LocalDateTime {
    return Instant.ofEpochSecond(epochSeconds)
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()
}





