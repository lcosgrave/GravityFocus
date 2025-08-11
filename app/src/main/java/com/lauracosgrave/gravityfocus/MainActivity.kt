package com.lauracosgrave.gravityfocus

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lauracosgrave.gravityfocus.navigation.AppNavigation
import com.lauracosgrave.gravityfocus.ui.theme.GravityFocusTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("ViewModelConstructorInComposable")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            val timerViewModel: TimerViewModel = viewModel()
            val repository = (application as GravityFocusApplication).repository

            val timerSessionViewModel = TimerSessionViewModel(repository)
            GravityFocusTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(timerViewModel, timerSessionViewModel)
                }
            }
        }
    }
}





