package com.lauracosgrave.gravityfocus

import android.app.Application
import com.lauracosgrave.gravityfocus.data.TimerDatabase
import com.lauracosgrave.gravityfocus.data.TimerSessionRepository

class GravityFocusApplication : Application() {

    val repository by lazy {
        TimerSessionRepository(
            TimerDatabase.getDatabase(this).timerSessionDao()
        )
    }
    
}