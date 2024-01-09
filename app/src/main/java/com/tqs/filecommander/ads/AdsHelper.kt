package com.tqs.filecommander.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.tqs.filecommander.R
import com.tqs.filecommander.base.BaseActivity
import com.tqs.filecommander.base.BaseAds
import com.tqs.filecommander.tba.EventPoints
import com.tqs.filecommander.tba.TBAHelper
import com.tqs.filecommander.utils.logD
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class AdsHelper(private val adsType: AdsItemType) {
    private val TAG = "AdsHelper"
    private val source: MutableList<AdsItem> = mutableListOf()
    private val cache: MutableList<BaseAds> = arrayListOf()
    private var isAdsLoading: Boolean = false
    private var onAdsLoad: (Boolean) -> Unit = {}
    val isCacheNotEmpty: Boolean
        get() = cache.isNotEmpty()

    private fun getAdsCache(): BaseAds? = cache.removeFirstOrNull()

    fun initializeSource(data: MutableList<AdsItem>?) {
        source.run {
            clear()
            addAll(data ?: mutableListOf())
            sortByDescending { it.adsWeight }
            forEach {
                "source = ${it.adsId} ${it.adsWeight} ${it.adsPlatform} ${it.adsType} ${it.adsAliveMillis}  ".logD()
            }
        }

    }

    private fun isCacheOverTime(): Boolean {
        val item = cache.firstOrNull() ?: return false
        return if (System.currentTimeMillis() - item.adsLoadTime >= item.adsItem.adsAliveMillis * 6000L) {
            cache.remove(item)
            true
        } else {
            false
        }
    }

    fun preLoad(context: Context) {
        cache.forEach {
            "cache = {\n ${it.adsItem.adsId} \n ${it.adsItem.adsType} \n ${it.adsItem.adsWeight} \n ${it.adsItem.adsPlatform}\n}".logD()
        }
        CoroutineScope(Dispatchers.Main + SupervisorJob() + CoroutineExceptionHandler { _, throwable ->
            Log.e(
                TAG,
                "${throwable.message}"
            )
        }).launch {
            if (source.isEmpty()) return@launch
            if (AdsManager.isOverLimit()) return@launch
            if (isCacheNotEmpty && isCacheOverTime().not()) return@launch
            if (isAdsLoading) return@launch
            isAdsLoading = true
            AdsPreloader(context, adsType, source, cache) {
                isAdsLoading = false
                onAdsLoad.invoke(it)
            }.startPreload()
        }
    }

    fun withLoad(context: Context, block: (Boolean) -> Unit = {}) {
        if (isCacheNotEmpty && isCacheOverTime().not())
            block.invoke(true)
        else {
            onAdsLoad = block
            preLoad(context)
        }
    }


    fun showFullScreenAds(activity: BaseActivity<*, *>, onAdsDismissed: () -> Unit) {

        val contentView =
            activity.window.decorView.findViewById<View>(android.R.id.content) as ViewGroup
        val customView =
            LayoutInflater.from(activity).inflate(R.layout.layout_ads_loading, contentView, false)
        contentView.addView(customView)

        activity.lifecycleScope.launch {
            delay(1000)
            TBAHelper.updatePoints(
                EventPoints.filec_ad_chance,
                mutableMapOf(EventPoints.ad_pos_id to adsType.adsItemType)
            )
            if (cache.isEmpty()) {
                contentView.removeView(customView)
                onAdsDismissed.invoke()
                return@launch
            }
            val baseAd = getAdsCache()
            if (null == baseAd) {
                contentView.removeView(customView)
                onAdsDismissed.invoke()
                return@launch
            }

            contentView.removeView(customView)
            baseAd.show(activity = activity, onAdsDismissed = onAdsDismissed)
            onAdsLoad = {}
            preLoad(activity)
        }

    }

    fun showNativeAds(activity: Activity, parent: ViewGroup?, onBaseAds: (BaseAds) -> Unit) {
        TBAHelper.updatePoints(
            EventPoints.filec_ad_chance,
            mutableMapOf(EventPoints.ad_pos_id to adsType.adsItemType)
        )
        val baseAd = getAdsCache() ?: return
        baseAd.show(activity = activity, nativeParent = parent)
        onBaseAds.invoke(baseAd)
        onAdsLoad = {}
        preLoad(activity)
    }
}