package com.tqs.filecommander.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import com.tqs.filecommander.notification.NotificationHelper
import com.tqs.filecommander.tba.EventPoints
import com.tqs.filecommander.tba.TBAHelper
import com.tqs.filecommander.utils.application
import com.tqs.filecommander.utils.logE
import com.tqs.filecommander.utils.toast

val broadcast by lazy {
    object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                Intent.ACTION_BATTERY_CHANGED -> {
                    NotificationHelper.createNotificationBroadcastCharge(application)
                }

                Intent.ACTION_USER_PRESENT -> {
                    NotificationHelper.createNotificationBroadcastUnlock(application)
                }

                Intent.ACTION_PACKAGE_REMOVED -> {
                    NotificationHelper.createNotificationBroadcastUninstall(application)
                }
            }
        }
    }
}

fun registerBattery(context: Context?) {
    context?.registerReceiver(broadcast, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
}

fun registerUnlock(context: Context?) {
    context?.registerReceiver(broadcast, IntentFilter(Intent.ACTION_USER_PRESENT))
}

fun registerUninstall(context: Context?) {
    context?.registerReceiver(broadcast, IntentFilter(Intent.ACTION_PACKAGE_REMOVED).apply {
        addDataScheme("package")
    })
}

fun registerNotificationReceiver(context: Context?){
    context?.registerReceiver(NotificationClickReceiver(),IntentFilter())
}
