package com.tqs.filecommander.base

import android.app.Activity
import android.view.ViewGroup
import com.blankj.utilcode.util.TimeUtils
import com.google.android.gms.ads.AdValue
import com.google.android.gms.ads.ResponseInfo
import com.google.firebase.analytics.FirebaseAnalytics
import com.tqs.filecommander.ads.ADLTV
import com.tqs.filecommander.ads.AdsItem
import com.tqs.filecommander.ads.AdsItemType
import com.tqs.filecommander.mmkv.MMKVHelper
import com.tqs.filecommander.net.adsUserCost
import com.tqs.filecommander.net.logEvent
import com.tqs.filecommander.tba.EventPoints
import com.tqs.filecommander.tba.TBAHelper
import com.tqs.filecommander.utils.logE

abstract class BaseAds(
    private val adsType: AdsItemType,
    val adsItem: AdsItem,
    var adsLoadTime: Long = System.currentTimeMillis()
) {
    abstract fun load(onAdsLoaded: () -> Unit = {}, onAdsLoadFailed: (msg: String?) -> Unit = {})
    abstract fun show(activity: Activity, nativeParent: ViewGroup? = null, onAdsDismissed: () -> Unit = {})

    //paid data to firebase
    fun onAdsPaid(adsValue: AdValue, item: AdsItem, responseInfo: ResponseInfo?) {
        "Debug Logcat: Advertising adsType = ${adsType.adsItemType}  type = ${item.adsType} Platform = ${item.adsPlatform}  ID = ${item.adsId} onPaid".logE()
        // data update to firebase
        runCatching {
            logEvent(
                "ad_impression_revenue",
                mutableMapOf(
                    FirebaseAnalytics.Param.VALUE to adsValue.valueMicros / 1000000.toDouble(),
                    FirebaseAnalytics.Param.CURRENCY to "USD",
//                    "precisionType" to adsValue.precisionType,
//                    "adNetwork" to responseInfo?.mediationAdapterClassName
                )
            )

            TBAHelper.updatePoints(EventPoints.filec_ad_impression, mutableMapOf(EventPoints.ad_pos_id to adsType.adsItemType))
        }
        // google 2.5
        runCatching {
            val currentCost =
                if (TimeUtils.isToday(MMKVHelper.currentUserCostTime)) {
                    MMKVHelper.currentUserCost + adsValue.valueMicros / 1000000.toDouble()
                } else {
                    adsValue.valueMicros / 1000000.toDouble()
                }

            if (adsUserCost?.top50!! in MMKVHelper.currentUserCost..<currentCost) {
                logEvent(
                    ADLTV.AdLTV_Top50Percent, mutableMapOf(
                        FirebaseAnalytics.Param.VALUE to adsUserCost?.top50,
                        FirebaseAnalytics.Param.CURRENCY to "USD",
                    )
                )
            }

            if (adsUserCost?.top40!! in MMKVHelper.currentUserCost..<currentCost) {
                logEvent(
                    ADLTV.AdLTV_Top40Percent, mutableMapOf(
                        FirebaseAnalytics.Param.VALUE to adsUserCost?.top40,
                        FirebaseAnalytics.Param.CURRENCY to "USD",
                    )
                )
            }

            if (adsUserCost?.top30!! in MMKVHelper.currentUserCost..<currentCost) {
                logEvent(
                    ADLTV.AdLTV_Top30Percent, mutableMapOf(
                        FirebaseAnalytics.Param.VALUE to adsUserCost?.top30,
                        FirebaseAnalytics.Param.CURRENCY to "USD",
                    )
                )
            }

            if (adsUserCost?.top20!! in MMKVHelper.currentUserCost..<currentCost) {
                logEvent(
                    ADLTV.AdLTV_Top20Percent, mutableMapOf(
                        FirebaseAnalytics.Param.VALUE to adsUserCost?.top20,
                        FirebaseAnalytics.Param.CURRENCY to "USD",
                    )
                )
            }

            if (adsUserCost?.top10!! in MMKVHelper.currentUserCost..<currentCost) {
                logEvent(
                    ADLTV.AdLTV_Top10Percent, mutableMapOf(
                        FirebaseAnalytics.Param.VALUE to adsUserCost?.top10,
                        FirebaseAnalytics.Param.CURRENCY to "USD",
                    )
                )
            }

            MMKVHelper.currentUserCost = currentCost
            MMKVHelper.currentUserCostTime = System.currentTimeMillis()
        }

        // google 3.0 need count and clean

        // todo data update to operate platform ad_impression
        TBAHelper.updateAdvertising(adsType = item.adsType, adsId = item.adsId, adsIndex = adsType.adsItemType, adsSDK = item.adsPlatform)
    }

    open fun destroyNative() {}
}