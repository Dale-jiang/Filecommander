package com.tqs.filecommander.ads

import com.blankj.utilcode.util.TimeUtils
import com.google.gson.Gson
import com.tqs.filecommander.mmkv.MMKVHelper


object AdsManager {
    const val TAG = "AdsManager"
    var showAdsCount = 0
    var clickAdsCount = 0
    var adsEntity: AdsEntity? = null
    var adsFullScreen = AdsHelper(AdsItemType.ADSFULLSCREEN)
    var adsInsertResultScan = AdsHelper(AdsItemType.ADSINSERTRESULTSCAN)
    var adsNativeResultScan = AdsHelper(AdsItemType.ADSNATIVERESULTSCAN)

    fun initAdsConfig(adsJson: String) {
        adsEntity = Gson().fromJson(adsJson, AdsEntity::class.java)
        showAdsCount = adsEntity?.showMax ?: 0
        clickAdsCount = adsEntity?.clickMax ?: 0
        adsFullScreen.initializeSource(adsEntity?.adsFullScreen)
        adsInsertResultScan.initializeSource(adsEntity?.adsInsertResultScan)
        adsNativeResultScan.initializeSource(adsEntity?.adsNativeResultScan)
    }

    fun isOverLimit(): Boolean {
        return clickOverLimit() || showOverLimit()
    }

    private fun showOverLimit(): Boolean {
        if (0 == showAdsCount) return false
        val showData = MMKVHelper.showAdsData ?: return false

        return if (TimeUtils.isToday(showData.time)) showData.count >= showAdsCount else false
    }

    private fun clickOverLimit(): Boolean {
        if (0 == clickAdsCount) return false
        val clickData = MMKVHelper.clickAdsData ?: return false

        return if (TimeUtils.isToday(clickData.time)) clickData.count >= clickAdsCount else false
    }

    fun addShowCount() {
        runCatching {
            val showData = MMKVHelper.showAdsData
            if (null == showData) {
                MMKVHelper.showAdsData = AdsCount()
            } else {
                if (TimeUtils.isToday(showData.time)) {
                    showData.count++
                    MMKVHelper.showAdsData = showData
                } else
                    MMKVHelper.showAdsData = AdsCount()
            }
        }
    }

    fun addClickCount() {
        runCatching {
            val clickData = MMKVHelper.clickAdsData
            if (null == clickData) {
                MMKVHelper.clickAdsData = AdsCount()
            } else {
                if (TimeUtils.isToday(clickData.time)) {
                    clickData.count++
                    MMKVHelper.clickAdsData = clickData
                } else
                    MMKVHelper.clickAdsData = AdsCount()
            }
        }
    }
}