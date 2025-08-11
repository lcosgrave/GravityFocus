package com.lauracosgrave.gravityfocus.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TimerSession::class], version = 1)
abstract class TimerDatabase : RoomDatabase() {
    abstract fun timerSessionDao(): TimerSessionDAO

    companion object {
        @Volatile
        private var INSTANCE: TimerDatabase? = null
        fun getDatabase(context: Context): TimerDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TimerDatabase::class.java,
                    "gravity_focus_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}