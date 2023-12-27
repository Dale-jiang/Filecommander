package com.tqs.filecommander.service

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.tqs.filecommander.broadcast.broadcast
import com.tqs.filecommander.notification.NotificationHelper

class ForeService : Service() {
    private val notificationID = 1
    private var notification: Notification? = null
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        startForeground(notificationID, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(STOP_FOREGROUND_REMOVE)
    }
}