package com.tqs.filecommander.ui.activity

import android.app.NotificationManager
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.tqs.filecommander.R
import com.tqs.filecommander.ads.AdsManager
import com.tqs.filecommander.base.BaseActivity
import com.tqs.filecommander.databinding.ActivityAdsOpenBinding
import com.tqs.filecommander.notification.NotificationHelper
import com.tqs.filecommander.notification.NotificationKey
import com.tqs.filecommander.tba.EventPoints
import com.tqs.filecommander.tba.TBAHelper
import com.tqs.filecommander.utils.Common
import com.tqs.filecommander.utils.logE
import com.tqs.filecommander.vm.MainVM

class AdsOpenActivity : BaseActivity<ActivityAdsOpenBinding, MainVM>() {
    override val layoutId: Int
        get() = R.layout.activity_ads_open
    override val TAG: String
        get() = "AdsOpenActivity"


    override fun initData() {
        viewModel = ViewModelProvider(this)[MainVM::class.java]
        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).apply {
            when (taskId) {
                NotificationHelper.requestScheduledCode,
                NotificationHelper.requestBatteryCode,
                NotificationHelper.requestUnlockCode,
                NotificationHelper.requestUninstallCode -> cancel(taskId)
            }
        }
        val notifyType = intent.getStringExtra(NotificationKey.notifyType)
        "notifyType = $notifyType".logE()
        AdsManager.adsFullScreen.showFullScreenAds(this) {
            jumpScannerActivity(Common.pageArray[(0..4).random()],notifyType)
            finish()
        }
        TBAHelper.updatePoints(EventPoints.filecpop_all_cli)
        TBAHelper.updatePoints(
            when (notifyType) {
                NotificationKey.SCHEDULED -> EventPoints.filecpop_t_cli
                NotificationKey.CHARGE -> EventPoints.filecpop_char_cli
                NotificationKey.UNCLOCK -> EventPoints.filecpop_unl_cli
                NotificationKey.UNINSTALL -> EventPoints.filecpop_uninstall_cli

                else -> {
                    ""
                }
            }
        )

    }
}