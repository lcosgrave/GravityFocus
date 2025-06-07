package com.lauracosgrave.gravityfocus

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.os.Process
import android.provider.Settings
import android.util.Log
import androidx.core.content.ContextCompat.startActivity

class UsagePermissionHandler(val context: Context) {
    var checkedHasPermission = false
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
        checkedHasPermission = true
        return checkPermissionResult
    }

    fun requestPermission(): Boolean {
        if (checkedHasPermission and !hasPermission) {
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            startActivity(context, intent, null)
        }
        return false
    }
}