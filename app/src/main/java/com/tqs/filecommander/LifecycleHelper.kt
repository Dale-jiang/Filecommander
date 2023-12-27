package com.tqs.filecommander

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import com.google.android.gms.ads.AdActivity
import com.tqs.filecommander.ads.AdsManager
import com.tqs.filecommander.ui.activity.ScannerActivity
import com.tqs.filecommander.ui.activity.SplashActivity
import com.tqs.filecommander.utils.application
import com.tqs.filecommander.utils.logE

var runningActivities = 0
var inBackgroundTime = 0L

class FileCommanderLifecycle : Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        "Lifecycle onActivityCreated ${activity.localClassName}".logE()
    }

    override fun onActivityStarted(activity: Activity) {
        "Lifecycle onActivityStarted ${activity.localClassName}".logE()
        if (runningActivities == 0) {
            // application from background to foreground
        }

        if (runningActivities == 0 && inBackgroundTime != 0L && System.currentTimeMillis() - inBackgroundTime > 3000) {
            "FileCommanderLifecycle ${activity is ScannerActivity} ".logE()
            if ( activity is ScannerActivity) {

            } else {
                startSplashActivity()
            }
        }

        runningActivities++
    }

    private fun startSplashActivity() {
        application.startActivity(Intent(application, SplashActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        })
    }

    override fun onActivityResumed(activity: Activity) {
        "Lifecycle onActivityResumed ${activity.localClassName}".logE()

    }

    override fun onActivityPaused(activity: Activity) {
        "Lifecycle onActivityPaused ${activity.localClassName}".logE()

    }

    override fun onActivityStopped(activity: Activity) {
        "Lifecycle onActivityStopped ${activity.localClassName}".logE()
        runningActivities--
        if (runningActivities == 0) {
            // application from fore to background
        }
        inBackgroundTime = System.currentTimeMillis()
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        "Lifecycle onActivitySaveInstanceState ${activity.localClassName}".logE()
    }

    override fun onActivityDestroyed(activity: Activity) {
        "Lifecycle onActivityDestroyed ${activity.localClassName}".logE()

    }



}