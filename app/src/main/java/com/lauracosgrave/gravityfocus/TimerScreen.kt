package com.lauracosgrave.gravityfocus

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun TimerScreen(timerViewModel: TimerViewModel, context: Context, modifier: Modifier = Modifier) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        val disallowedUsageHappened by timerViewModel.disallowedUsageHappened.collectAsState()

        var timerPickerDialogOpen by rememberSaveable { mutableStateOf(false) }
        val startTimeMins by timerViewModel.startTimeMins.collectAsState()
        val time by timerViewModel.time.collectAsState()
        val timerRunning by timerViewModel.timerRunning.collectAsState()

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Text("Focus with GravityFocus", fontSize = 24.sp, modifier = modifier.padding(16.dp))
            if (timerPickerDialogOpen) {
                TimerPickerDialog(startTimeMins, onConfirm = { newTime ->
                    timerViewModel.updateStartTime(newTime); timerPickerDialogOpen = false
                }, onDismissRequest = { timerPickerDialogOpen = false })
            }
            if (disallowedUsageHappened) {
                FailedTimerDialog(onDismissRequest = { timerViewModel.resetTimer() })
            }
            Timer(
                time,
                timerRunning,
                timerViewModel,
                { timerViewModel.stopTimer(); timerPickerDialogOpen = true; },
                context,
            )
        }
    }

}



