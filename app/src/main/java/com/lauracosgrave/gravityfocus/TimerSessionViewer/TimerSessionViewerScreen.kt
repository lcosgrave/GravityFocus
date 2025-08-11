package com.lauracosgrave.gravityfocus.TimerSessionViewer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.lauracosgrave.gravityfocus.TimerSessionViewModel
import com.lauracosgrave.gravityfocus.data.TimerSession

@Composable
fun TimerSessionViewerScreen(timerSessionViewModel: TimerSessionViewModel) {

    val lazyPagingTimerSessions: LazyPagingItems<TimerSession> =
        timerSessionViewModel.timerSessionPagingFlow.collectAsLazyPagingItems()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Session History", fontSize = 30.sp, modifier = Modifier.padding(16.dp))
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                // show items
                // TODO: Next step: format items
                items(
                    lazyPagingTimerSessions.itemCount,
                    key = lazyPagingTimerSessions.itemKey { it.id }) { index ->
                    // item can be null during placeholder/loading
                    index?.let {
                        TimerSessionCard(lazyPagingTimerSessions[index])
                    }
                }

            }
        }
    }
}