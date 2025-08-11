package com.lauracosgrave.gravityfocus.Timer

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lauracosgrave.gravityfocus.TimerViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Timer(
    time: Int,
    timerRunning: Boolean,
    timerViewModel: TimerViewModel,
    hasUsageStatsPermission: Boolean,
    editStartTime: () -> Unit,
    modifier: Modifier = Modifier
) {
    val settingsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { timerViewModel.onPermissionResult() }

    fun requestPermissionLauncher() {
        val intent = timerViewModel.requestUsageStatsPermission()
        if (intent != null) {
            settingsLauncher.launch(intent)
        }
    }

    Surface(modifier = Modifier.wrapContentSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(onClick = {
                editStartTime(); Log.d("TimerScreen", "time: ${time}")
            }, modifier = modifier.padding(16.dp)) {
                Text(displayTime(time), fontSize = 40.sp, modifier = modifier.padding(16.dp))
            }
            Button(
                onClick = {
                    if (hasUsageStatsPermission) {
                        timerViewModel.startOrStopTimer()
                        Log.d("TimerScreen", "time: ${time}")
                    } else {
                        requestPermissionLauncher()
                    }

                }, modifier = Modifier
                    .wrapContentSize()
            ) {
                if (timerRunning && time > 0) {
                    Text("Stop")
                } else {
                    Text("Start")
                }

            }
            Button(
                onClick = {
                    timerViewModel.stopTimer()
                    timerViewModel.resetTimer()
                    Log.d("TimerScreen", "time: ${time}")
                },
                modifier = Modifier
                    .wrapContentSize(),
            ) {
                Text("Reset")
            }
        }
    }
}

fun displayTime(totalSeconds: Int): String {
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    return String.format(Locale.UK, "%02d:%02d:%02d", hours, minutes, seconds)
}

