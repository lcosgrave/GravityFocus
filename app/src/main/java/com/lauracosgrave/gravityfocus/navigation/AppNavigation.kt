package com.lauracosgrave.gravityfocus.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lauracosgrave.gravityfocus.R
import com.lauracosgrave.gravityfocus.Timer.TimerScreen
import com.lauracosgrave.gravityfocus.TimerSessionViewModel
import com.lauracosgrave.gravityfocus.TimerSessionViewer.TimerSessionViewerScreen
import com.lauracosgrave.gravityfocus.TimerViewModel
import kotlinx.serialization.Serializable

@Serializable
object Timer

@Serializable
object TimerSessionViewer

data class TopLevelRoute<T : Any>(val name: String, val route: T, val iconPainter: Painter)


@Composable
fun AppNavigation(timerViewModel: TimerViewModel, timerSessionViewModel: TimerSessionViewModel) {
    val topLevelRoutes = listOf(
        TopLevelRoute(
            "Timer",
            Timer,
            painterResource(R.drawable.timer_24px)
        ),
        TopLevelRoute(
            "History",
            TimerSessionViewer,
            painterResource(R.drawable.view_list_24px)
        )
    )
    var selectedRoute by rememberSaveable { mutableStateOf(0) }
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            NavigationBar(modifier = Modifier.fillMaxWidth()) {
                topLevelRoutes.forEachIndexed { index, topLevelRoute ->
                    NavigationBarItem(
                        selected = index == selectedRoute,
                        icon = {
                            Icon(
                                painter = topLevelRoute.iconPainter,
                                contentDescription = topLevelRoute.name
                            )
                        },
                        label = { Text(topLevelRoute.name) },
                        onClick = {
                            navController.navigate(route = topLevelRoute.route)
                            selectedRoute = index
                        }
                    )
                }
            }
        },
        content = { padding ->
            NavHost(
                navController = navController,
                startDestination = Timer,
                Modifier.padding(padding)
            ) {
                composable<Timer> { TimerScreen(timerViewModel) }
                composable<TimerSessionViewer> { TimerSessionViewerScreen(timerSessionViewModel) }
            }
        }
    )
}