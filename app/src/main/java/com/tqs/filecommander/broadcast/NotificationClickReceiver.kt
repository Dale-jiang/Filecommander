package com.tqs.filecommander.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.tqs.filecommander.notification.NotificationKey
import com.tqs.filecommander.ui.activity.MainActivity
import com.tqs.filecommander.utils.application
import com.tqs.filecommander.utils.logE

class NotificationClickReceiver:BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        "NotificationClickReceiver click notification ".logE()
        application.startActivity(Intent(application, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra(NotificationKey.notifyType, intent?.getStringExtra(NotificationKey.notifyType))
        })

    }
}