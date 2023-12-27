package com.tqs.filecommander.ads

import android.content.Context
import com.tqs.filecommander.base.BaseAds
import com.tqs.filecommander.tba.EventPoints
import com.tqs.filecommander.tba.TBAHelper
import com.tqs.filecommander.utils.logD

class AdsPreloader(
    private val context: Context,
    private val adsType: AdsItemType,
    private val source: MutableList<AdsItem>,
    private val cache: MutableList<BaseAds>,
    private val onLoad: (Boolean) -> Unit = {}
) {
    fun startPreload() {
        if (source.isEmpty()) {
            onLoad.invoke(false)
        } else {
            preloadAdsByIndex(0)
        }
    }

    private fun preloadAdsByIndex(index: Int) {
        val item = source.getOrNull(index)
        if (null == item) {
            onLoad.invoke(false)
            return
        }
        "ADS Preloader load pre source $index = ${item.adsId} ${item.adsWeight} ${item.adsPlatform} ${item.adsType} ${item.adsAliveMillis} ".logD()
        val baseAds = when (item.adsType) {
            ADSType.ADS_TYPE_INT, ADSType.ADS_TYPE_OP -> FullScreenAdvertising(context, adsType, item)
            ADSType.ADS_TYPE_NAT -> NativeMainAdvertising(context, adsType, item)
            else -> null
        }
        if (null == baseAds) {
            onLoad.invoke(false)
            return
        }
        TBAHelper.updatePoints(EventPoints.filec_ad_request, mutableMapOf(EventPoints.ad_pos_id to adsType.adsItemType))
        baseAds.load(onAdsLoaded = {
            cache.add(baseAds)
            cache.sortByDescending { it.adsItem.adsWeight }
            onLoad.invoke(true)
            cache.forEach{
                "ADS Preloader load success cache = {${it.adsItem.adsId} ${it.adsItem.adsType} ${it.adsItem.adsWeight} ${it.adsItem.adsPlatform}\n}".logD()
            }
        }, onAdsLoadFailed = {
            "ADS Preloader load fail ($it)  $index  ${item.adsId} ${item.adsWeight} ${item.adsPlatform} ${item.adsType} ${item.adsAliveMillis} ".logD()
            preloadAdsByIndex(index + 1)
        })
    }

}