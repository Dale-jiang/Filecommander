package com.tqs.filecommander.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.blankj.utilcode.util.TimeUtils
import com.google.gson.Gson
import com.tqs.filecommander.cloak.CloakHelper
import com.tqs.filecommander.cloak.CloakKey
import com.tqs.filecommander.mmkv.MMKVHelper
import com.tqs.filecommander.referrer.ReferrerHelper
import com.tqs.filecommander.runningActivities
import com.tqs.filecommander.ui.activity.ScannerActivity
import com.tqs.filecommander.utils.Common
import com.tqs.filecommander.utils.application
import com.tqs.filecommander.utils.getJsonFromAssets
import com.tqs.filecommander.utils.logE
import java.util.Timer
import java.util.TimerTask

object NotificationController {

    private var notificationControl = 0
    private var notificationMap: MutableMap<String, NotificationItem> = mutableMapOf()
    private var showedMap: MutableMap<String, NotificationShowed> = mutableMapOf()
    private var timer: Timer? = null
    fun setNotification(notificationEntity: NotificationEntity) {
        notificationControl = notificationEntity.notificationSwitch
        ReferrerHelper.referrerControl = notificationEntity.referrerSwitch
        notificationMap.clear()
        notificationMap[NotificationKey.SCHEDULED] = notificationEntity.timing
        notificationMap[NotificationKey.UNCLOCK] = notificationEntity.unclock
        notificationMap[NotificationKey.UNINSTALL] = notificationEntity.uninstall
        notificationMap[NotificationKey.CHARGE] = notificationEntity.battery

        if (timer == null) {
            openTimingNotification()
        }
    }

    private fun openTimingNotification() {
        timer = Timer()
        timer?.schedule(
            object : TimerTask() {
                override fun run() {
                    "Debug Logcat: Notification Scheduled preparing show".logE()
                    NotificationHelper.createNotificationScheduled(application)
                }
            }, notificationMap[NotificationKey.SCHEDULED]?.delayPopupTime!! * 1000L * 60,
            1000L * 60 * notificationMap[NotificationKey.SCHEDULED]?.intervalPopupTime!!
        )
    }

    fun initNotificationConfig(context: Context) {
        if (notificationMap.isEmpty()) {
            Gson().fromJson<NotificationEntity>(
                getJsonFromAssets(context, "notificationConfig.json"),
                NotificationEntity::class.java
            ).let {
                setNotification(it)
            }
        }
        if (showedMap.isNotEmpty()) return
        Gson().fromJson<NotificationConfig>(
            getJsonFromAssets(context, "notificationContent.json"),
            NotificationConfig::class.java
        ).apply {
            for (item in this.scenes) {
                val key = when (item.name) {
                    NotificationKey.Scheduled -> {
                        MMKVHelper.normalNotification = NotificationShowed().apply {
                            title = item.name
                            content = item.notification
                        }
                        NotificationKey.SCHEDULED
                    }

                    NotificationKey.Unlock -> {
                        MMKVHelper.unlockNotification = NotificationShowed().apply {
                            title = item.name
                            content = item.notification
                        }
                        NotificationKey.UNCLOCK
                    }

                    NotificationKey.Uninstall -> {
                        MMKVHelper.uninstallNotification = NotificationShowed().apply {
                            title = item.name
                            content = item.notification
                        }
                        NotificationKey.UNINSTALL
                    }

                    NotificationKey.Charge -> {
                        MMKVHelper.batteryNotification = NotificationShowed().apply {
                            title = item.name
                            content = item.notification
                        }
                        NotificationKey.CHARGE
                    }

                    else -> {
                        ""
                    }
                }
                updateShowedNotification(key)
//                updateMMKVNotification(key)

            }
        }
    }

    private fun updateShowedNotification(key: String) {
        when (key) {
            NotificationKey.SCHEDULED -> showedMap[key] = MMKVHelper.normalNotification as NotificationShowed
            NotificationKey.UNCLOCK -> showedMap[key] = MMKVHelper.unlockNotification as NotificationShowed
            NotificationKey.UNINSTALL -> showedMap[key] = MMKVHelper.uninstallNotification as NotificationShowed
            NotificationKey.CHARGE -> showedMap[key] = MMKVHelper.batteryNotification as NotificationShowed
        }
    }

    private fun updateMMKVNotification(key: String) {
        showedMap[key]?.let {
            when (key) {
                NotificationKey.SCHEDULED -> MMKVHelper.normalNotification = it
                NotificationKey.UNCLOCK -> MMKVHelper.unlockNotification = it
                NotificationKey.UNINSTALL -> MMKVHelper.uninstallNotification = it
                NotificationKey.CHARGE -> MMKVHelper.batteryNotification = it
            }
        }
    }

    fun updateShowTimes(key: String) {
        showedMap[key]?.let {
            if (TimeUtils.isToday(it.lastShowTime)) {
                it.showTimes++
            } else {
                it.showTimes = 1
            }
            it.lastShowTime = System.currentTimeMillis()
        }
        updateMMKVNotification(key)
    }

    private fun isLimit(key: String): Boolean {
        "notification = ${notificationMap[key]?.dayShowLimit}".logE()
        return notificationMap[key]?.let {
            when (it.dayShowLimit) {
                0 -> false
                showedMap[key]?.showTimes -> true
                null -> false
                else -> false
            }
        } ?: false
    }

    /**
     * last showed time is or not more interval time
     */
    private fun isMoreIntervalTime(key: String): Boolean {
        return System.currentTimeMillis() - showedMap[key]?.lastShowTime!! >
                if (showedMap[key]?.showTimes == 0) {
                    notificationMap[key]?.delayPopupTime!!
                } else {
                    notificationMap[key]?.intervalPopupTime!!
                } * 1000 * 60

    }

    fun isOpenPopup(key: String): Boolean {
        return notificationMap[key]?.isPopup == 1
    }

    fun getNotificationName(key: String): String? {
        return showedMap[key]?.title
    }


    fun getNotificationContent(key: String): String? {
        return showedMap[key]?.content
    }

    fun isShouldShowNotification(type: String): Boolean {

        // if application in foreground return false
        "Debug Logcat: Notification $type runningActivities = ${runningActivities}".logE()
        if (runningActivities != 0) return false
        "Debug Logcat: Notification $type CloakHelper.cloakState = ${CloakHelper.cloakState}".logE()
        if (CloakHelper.cloakState == CloakKey.poem) return false
        "Debug Logcat: Notification $type referrerControl = ${ReferrerHelper.referrerControl} and ${ReferrerHelper.isReferrerUser()}".logE()
        if (ReferrerHelper.isReferrerUser().not()) return false
        "Debug Logcat: Notification $type notificationControl = $notificationControl".logE()
        if (notificationControl != 1) return false
        "Debug Logcat: Notification $type isLimit = ${isLimit(type)}".logE()
        if (isLimit(type)) return false
        "Debug Logcat: Notification $type isMoreIntervalTime = ${isMoreIntervalTime(type)}".logE()
        return isMoreIntervalTime(type)
    }

    fun getPendingIntent(context: Context, code: Int, notifyType: String): PendingIntent {
        return PendingIntent.getActivity(context, code, getIntent(context, notifyType),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun getIntent(context: Context, notifyType: String): Intent {
        return Intent(context, ScannerActivity::class.java).apply {
            putExtra(NotificationKey.notifyType, notifyType)
            putExtra(Common.PAGE_TYPE,Common.pageArray[(0..4).random()])
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }

    }
}