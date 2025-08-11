package com.lauracosgrave.gravityfocus.Timer

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.os.Process
import android.provider.Settings
import android.util.Log

class UsagePermissionHandler(val context: Context) {
    var hasPermission = false

    fun checkHasPermission(): Boolean {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.unsafeCheckOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(),
            context.packageName
        )
        val checkPermissionResult = mode == AppOpsManager.MODE_ALLOWED
        Log.d("UsagePermissionHandler", "hasPermission: $checkPermissionResult")
        Log.d("UsagePermissionHandler", "permissionval: ${mode}")

        hasPermission = checkPermissionResult
        return checkPermissionResult
    }

    fun requestPermission(): Intent? {
        if (!hasPermission) {
            return Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
        }
        return null
    }
}