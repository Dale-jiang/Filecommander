package com.tqs.filecommander

import android.app.Application
import android.content.Intent
import android.os.Build
import com.tencent.mmkv.MMKV
import com.tqs.filecommander.ads.AdsManager
import com.tqs.filecommander.broadcast.registerBattery
import com.tqs.filecommander.broadcast.registerUninstall
import com.tqs.filecommander.broadcast.registerUnlock
import com.tqs.filecommander.cloak.CloakHelper
import com.tqs.filecommander.mmkv.MMKVHelper
import com.tqs.filecommander.net.RemoteHelper
import com.tqs.filecommander.notification.NotificationController
import com.tqs.filecommander.referrer.ReferrerHelper
import com.tqs.filecommander.service.ForeService
import com.tqs.filecommander.tba.EventPoints
import com.tqs.filecommander.tba.TBAHelper
import com.tqs.filecommander.utils.Common.defaultJson
import com.tqs.filecommander.utils.DateUtils
import com.tqs.filecommander.utils.application
import com.tqs.filecommander.utils.logE
import java.util.Timer
import java.util.TimerTask

class FileCommanderApp : Application() {
    override fun onCreate() {
        super.onCreate()
        application = this
        AdsManager.initAdsConfig(defaultJson)
        MMKV.initialize(this)
        NotificationController.initNotificationConfig(this)
        ReferrerHelper.initReferrer(this)
        TBAHelper.getAds { id, bool ->
            MMKVHelper.GaId = id
            "FileCommanderApp = ${MMKVHelper.GaId}".logE()
            MMKVHelper.isLimitAdTrackingEnabled = bool
            CloakHelper.getCloakConfig()
        }
//        FirebaseApp.initializeApp(this)
        if (BuildConfig.DEBUG.not())
           // RemoteHelper.fetch()
//        startService()
        registerBroadcast()
        timerPoints()
        if (0L == MMKVHelper.firstLaunchAppTime) {
            MMKVHelper.firstLaunchAppTime = System.currentTimeMillis()
        }
//        if (TimeUtils.isToday(MMKVHelper.launchAppTime).not()){
        TBAHelper.updatePoints(
            EventPoints.covenant,
            mutableMapOf(
                EventPoints.D to "D${
                    DateUtils.getMillisDay(System.currentTimeMillis()) - DateUtils.getMillisDay(
                        MMKVHelper.firstLaunchAppTime
                    )
                }"
            ),
            EventPoints.filec_retention,
        )
//        }
        MMKVHelper.launchAppTime = System.currentTimeMillis()
        registerActivityLifecycleCallbacks(FileCommanderLifecycle())
    }


    private fun timerPoints() {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                TBAHelper.updatePoints(EventPoints.filec_session_back)
            }
        }, 0, 30000)

    }

    private fun registerBroadcast() {
        registerBattery(this)
        registerUnlock(this)
        registerUninstall(this)
//        registerNotificationReceiver(this)
    }

    private fun startService() {
        Intent(this, ForeService::class.java).apply {
//            putExtra("","")
        }.let {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
                startForegroundService(it)
            } else {
                startService(it)
            }
        }

    }
}