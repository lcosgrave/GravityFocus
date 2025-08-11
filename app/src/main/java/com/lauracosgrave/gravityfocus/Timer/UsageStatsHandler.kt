package com.lauracosgrave.gravityfocus.Timer

import android.app.usage.UsageEvents
import android.app.usage.UsageEvents.Event
import android.app.usage.UsageStatsManager
import android.content.Context
import android.util.Log


class UsageStatsHandler(context: Context) {
    private val usageStatsManager =
        context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
    val excludedPackageNames = listOf<String>("android", "com.lauracosgrave.gravityfocus")
    val includedEvents =
        listOf<Int>(
            UsageEvents.Event.ACTIVITY_RESUMED,
            UsageEvents.Event.USER_INTERACTION,
            UsageEvents.Event.FOREGROUND_SERVICE_START
        )

    fun checkForDisallowedUsage(timeIntervalMillis: Int): Boolean {
        val usageEvents = getUsageEvents(timeIntervalMillis)
        val parsedUsageEvents = parseUsageEvents(usageEvents)
        Log.d("UsageStats", "usageEvents: ${parsedUsageEvents}")
        val filteredUsageEvents = filterParsedEvents(parsedUsageEvents)
        Log.d("UsageStats", "filteredUsageEvents: ${filteredUsageEvents}")
        val disallowedUsageHappened = filteredUsageEvents.size > 0
        Log.d("UsageStats", "disallowedUsageHappened: ${disallowedUsageHappened}")

        return disallowedUsageHappened
    }

    private fun getUsageEvents(timeIntervalMillis: Int): UsageEvents {
        val currentTime = System.currentTimeMillis()
        val usageEvents = usageStatsManager.queryEvents(
            currentTime - 1000 - timeIntervalMillis,
            currentTime - 1000
        )
        return usageEvents
    }

    private fun parseUsageEvents(usageEvents: UsageEvents): MutableList<ParsedEvent> {
        val parsedEvents = mutableListOf<ParsedEvent>()
        var eventOut = Event()
        for (i in 1..10) {
            if (usageEvents.getNextEvent(eventOut)) {

                parsedEvents.add(ParsedEvent(eventOut.packageName, eventOut.eventType))
            }
        }

        return parsedEvents
    }

    private fun filterParsedEvents(parsedEvents: MutableList<ParsedEvent>): List<ParsedEvent> {
        val filteredParsedEvents =
            parsedEvents.filter {
                (it.packageName !in excludedPackageNames)
                        && (it.eventType in includedEvents)
            } // don't get destroyed by notifications
        return filteredParsedEvents
    }
}

data class ParsedEvent(val packageName: String, val eventType: Int)
