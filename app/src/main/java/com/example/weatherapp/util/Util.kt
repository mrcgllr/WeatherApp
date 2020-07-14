package com.example.weatherapp.util

import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

object Util {
    @JvmStatic
    fun requestPermission(
        activity: AppCompatActivity, requestId: Int,
        permission: String
    ) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
        } else {
            // Location permission has not been granted yet, request it.
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(permission),
                requestId
            )
        }
    }
}