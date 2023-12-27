package com.tqs.filecommander.ads

import android.app.Activity
import android.content.Context
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.tqs.filecommander.base.BaseActivity
import com.tqs.filecommander.base.BaseAds
import com.tqs.filecommander.utils.logE
import com.tqs.filecommander.utils.logW
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FullScreenAdvertising(
    private val context: Context,
    private val adsType: AdsItemType,
    private val item: AdsItem
) : BaseAds(adsType, item) {
    private val adsRequest: AdRequest
        get() = AdRequest.Builder().build()
    private var ad: Any? = null
    override fun load(onAdsLoaded: () -> Unit, onAdsLoadFailed: (msg: String?) -> Unit) {
        "AdsOpen load".logE()
        when (item.adsType) {
            ADSType.ADS_TYPE_OP -> loadOpenAdvertising(onAdsLoaded, onAdsLoadFailed)
            ADSType.ADS_TYPE_INT -> loadInterstitial(onAdsLoaded, onAdsLoadFailed)
            ADSType.ADS_TYPE_NAT -> loadNativeAdvertising(onAdsLoaded, onAdsLoadFailed)
            else -> onAdsLoadFailed.invoke("ad type not right")
        }
    }

    override fun show(activity: Activity, nativeParent: ViewGroup?, onAdsDismissed: () -> Unit) {
        fun onAdsClose(){
            val baseActivity = activity as? BaseActivity<*, *>
            if (null != baseActivity){
                baseActivity.lifecycleScope.launch {
                    while (!baseActivity.isActivityOnResume()) delay(200L)
                    onAdsDismissed.invoke()
                }
            }else{
                onAdsDismissed.invoke()
            }

        }
        val callback: FullScreenContentCallback by lazy {
            object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    "Advertising callback 1 onAdDismissedFullScreenContent".logW()
                    onAdsClose()
                }

                override fun onAdShowedFullScreenContent() {
                    "Advertising callback 2 onAdShowedFullScreenContent".logW()
                    AdsManager.addShowCount()
                }

                override fun onAdFailedToShowFullScreenContent(e: AdError) {
                    "Advertising callback 3 onAdFailedToShowFullScreenContent".logW()
                    onAdsClose()
                }

                override fun onAdClicked() {
                    "Advertising callback 4 onAdClicked".logW()
                    AdsManager.addClickCount()
                }

                override fun onAdImpression() {
                    "Advertising callback 5 onAdImpression".logW()
                    super.onAdImpression()
                }
            }
        }

        fun showAdsFullScreen() {
            when (val ads = ad) {
                is InterstitialAd -> {
                    ads.run {
                        fullScreenContentCallback = callback
                        "Debug Logcat: Advertising adsType = ${adsType.adsItemType} type = ${item.adsType} Platform = ${item.adsPlatform}  ID = ${item.adsId} Showing".logE()
                        show(activity)
                    }
                }

                is AppOpenAd -> {
                    ads.run {
                        fullScreenContentCallback = callback
                        "Debug Logcat: Advertising adsType = ${adsType.adsItemType} type = ${item.adsType} Platform = ${item.adsPlatform}  ID = ${item.adsId} Showing".logE()
                        show(activity)
                    }
                }

                else -> onAdsDismissed.invoke()
            }
        }
        showAdsFullScreen()
    }

    private fun loadOpenAdvertising(onAdsLoaded: () -> Unit, onAdsLoadFailed: (msg: String?) -> Unit) {
        AppOpenAd.load(context, item.adsId, adsRequest, AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, object : AppOpenAd.AppOpenAdLoadCallback() {
            override fun onAdLoaded(appOpenAd: AppOpenAd) {
                "Debug Logcat: Advertising Loading success adsType = ${adsType.adsItemType} type = ${item.adsType} Platform = ${item.adsPlatform}  ID = ${item.adsId} ".logE()
                ad = appOpenAd
                adsLoadTime = System.currentTimeMillis()
                onAdsLoaded.invoke()
                appOpenAd.setOnPaidEventListener {
                    onAdsPaid(it, item, appOpenAd.responseInfo)
                }
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                "Debug Logcat: Advertising Loading fail adsType = ${adsType.adsItemType} type = ${item.adsType} Platform = ${item.adsPlatform}  ID = ${item.adsId} ".logE()
                onAdsLoadFailed.invoke(loadAdError.message)
            }
        })
    }

    private fun loadInterstitial(onAdsLoaded: () -> Unit, onAdsLoadFailed: (msg: String?) -> Unit) {
        InterstitialAd.load(context, item.adsId, adsRequest, object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                "Debug Logcat: Advertising Loading success adsType = ${adsType.adsItemType}  type = ${item.adsType} Platform = ${item.adsPlatform}  ID = ${item.adsId}".logE()
                ad = interstitialAd
                adsLoadTime = System.currentTimeMillis()
                onAdsLoaded.invoke()
                interstitialAd.setOnPaidEventListener {
                    onAdsPaid(it, item, interstitialAd.responseInfo)
                }
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                "Debug Logcat: Advertising Loading fail adsType = ${adsType.adsItemType} type = ${item.adsType} Platform = ${item.adsPlatform}  ID = ${item.adsId} ".logE()
                onAdsLoadFailed.invoke(loadAdError.message)
            }
        })
    }

    private fun loadNativeAdvertising(onAdsLoaded: () -> Unit, onAdsLoadFailed: (msg: String?) -> Unit) {

    }

}