package com.tqs.filecommander.notification

import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import com.tqs.filecommander.R
import com.tqs.filecommander.tba.EventPoints
import com.tqs.filecommander.tba.TBAHelper
import com.tqs.filecommander.utils.application

object NotificationHelper {
    const val requestUninstallCode = 1011
    const val requestScheduledCode = 1012
    const val requestBatteryCode = 1013
    const val requestUnlockCode = 1015

    private val notificationManager by lazy { application.getSystemService(Activity.NOTIFICATION_SERVICE) as NotificationManager }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(context: Context, channelId: String, channelName: String, important: Int) {
        notificationManager.createNotificationChannel(NotificationChannel(channelId, channelName, important).apply {
            canBypassDnd()
            lockscreenVisibility = Notification.VISIBILITY_SECRET
            canShowBadge()
            setBypassDnd(true)
        })
    }

    fun createNotificationScheduled(context: Context) {
        if (!NotificationController.isShouldShowNotification(NotificationKey.SCHEDULED)) return
        notificationManager.notify(
            requestScheduledCode,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel(context, "channel_id_scheduled", "channel_scheduled", NotificationManager.IMPORTANCE_HIGH)
                Notification.Builder(context, "channel_id_scheduled")
            } else {
                Notification.Builder(context)
            }.apply {
                setContentIntent(NotificationController.getPendingIntent(context, requestScheduledCode, NotificationKey.SCHEDULED))
                setAutoCancel(true)
                setSmallIcon(R.mipmap.ic_launcher_foreground)
                setWhen(System.currentTimeMillis())
                setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher_foreground))
//                setContentTitle(NotificationController.getNotificationName(NotificationKey.SCHEDULED))
                setContentText(NotificationController.getNotificationContent(NotificationKey.SCHEDULED))
            }.build()
        )
        NotificationController.updateShowTimes(NotificationKey.SCHEDULED)
        TBAHelper.updatePoints(EventPoints.filecpop_t_tri)
        TBAHelper.updatePoints(EventPoints.filecpop_all_tri)
    }

    fun createNotificationBroadcastUninstall(context: Context) {
        if (!NotificationController.isShouldShowNotification(NotificationKey.UNINSTALL)) return
        notificationManager.notify(
            requestUninstallCode,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel(context, "channel_id_uninstall", "channel_uninstall", NotificationManager.IMPORTANCE_HIGH)
                Notification.Builder(context, "channel_id_uninstall")
            } else {
                Notification.Builder(context)
            }.apply {
                setContentIntent(NotificationController.getPendingIntent(context, requestUninstallCode, NotificationKey.UNINSTALL))
                setAutoCancel(true)
                setSmallIcon(R.mipmap.ic_launcher_foreground)
                setWhen(System.currentTimeMillis())
                setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher_foreground))
//                setContentTitle(NotificationController.getNotificationName(NotificationKey.UNINSTALL))
                setContentText(NotificationController.getNotificationContent(NotificationKey.UNINSTALL))
            }.build()
        )
        NotificationController.updateShowTimes(NotificationKey.UNINSTALL)
        TBAHelper.updatePoints(EventPoints.filecpop_uninstall_tri)
        TBAHelper.updatePoints(EventPoints.filecpop_all_tri)
    }

    fun createNotificationBroadcastCharge(context: Context) {
        if (!NotificationController.isShouldShowNotification(NotificationKey.CHARGE)) return
        notificationManager.notify(
            requestBatteryCode,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel(context, "channel_id_battery", "channel_battery", NotificationManager.IMPORTANCE_HIGH)
                Notification.Builder(context, "channel_id_battery")
            } else {
                Notification.Builder(context)
            }.apply {
                setContentIntent(NotificationController.getPendingIntent(context, requestBatteryCode, NotificationKey.CHARGE))
                setAutoCancel(true)
                setSmallIcon(R.mipmap.ic_launcher_foreground)
                setWhen(System.currentTimeMillis())
                setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher_foreground))
//                setContentTitle(NotificationController.getNotificationName(NotificationKey.CHARGE))
                setContentText(NotificationController.getNotificationContent(NotificationKey.CHARGE))
            }.build()
        )
        NotificationController.updateShowTimes(NotificationKey.CHARGE)

        TBAHelper.updatePoints(EventPoints.filecpop_char_tri)
        TBAHelper.updatePoints(EventPoints.filecpop_all_tri)
    }

    fun createNotificationBroadcastUnlock(context: Context) {
        if (!NotificationController.isShouldShowNotification(NotificationKey.UNCLOCK)) return
        notificationManager.notify(
            requestUnlockCode,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel(context, "channel_id_unlock", "channel_unlock", NotificationManager.IMPORTANCE_HIGH)
                Notification.Builder(context, "channel_id_unlock")
            } else {
                Notification.Builder(context)
            }.apply {

                setContentIntent(NotificationController.getPendingIntent(context, requestUnlockCode, NotificationKey.UNCLOCK))
                setAutoCancel(true)
                setSmallIcon(R.mipmap.ic_launcher_foreground)
                setWhen(System.currentTimeMillis())
                setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher_foreground))
//                setContentTitle(NotificationController.getNotificationName(NotificationKey.UNCLOCK))
                setContentText(NotificationController.getNotificationContent(NotificationKey.UNCLOCK))
            }.build()
        )

        NotificationController.updateShowTimes(NotificationKey.UNCLOCK)

        TBAHelper.updatePoints(EventPoints.filecpop_unl_tri)
        TBAHelper.updatePoints(EventPoints.filecpop_all_tri)
    }


}