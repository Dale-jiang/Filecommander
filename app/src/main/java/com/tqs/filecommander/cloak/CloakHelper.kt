package com.tqs.filecommander.cloak

import android.os.Build
import com.tqs.filecommander.BuildConfig
import com.tqs.filecommander.mmkv.MMKVHelper
import com.tqs.filecommander.net.HttpHelper
import com.tqs.filecommander.tba.EventPoints
import com.tqs.filecommander.tba.TBAHelper
import com.tqs.filecommander.utils.encode
import com.tqs.filecommander.utils.getAndroidId
import com.tqs.filecommander.utils.logD
import com.tqs.filecommander.utils.logE
import org.json.JSONObject
import java.lang.StringBuilder

object CloakHelper {
    var cloakState = CloakKey.poem
    private var reloadTimes = 0
    fun getCloakConfig() {
        TBAHelper.updatePoints(EventPoints.filec_cloak_start)
        val startSecond = System.currentTimeMillis() / 1000
        HttpHelper.sendRequestGet(
            requestString = getConfig(),
            resultSuccess = {
                cloakState = it.ifBlank {
                    CloakKey.poem
                }

                MMKVHelper.cloakState = it
                reloadTimes = 0
                TBAHelper.updatePoints(
                    EventPoints.filec_cloak_get, mutableMapOf(
                        EventPoints.Time to System.currentTimeMillis() / 1000 - startSecond,
                        EventPoints.getsuccess to true,
                        EventPoints.source to cloakState
                    )
                )

            }, resultFailed = { _, _ ->
                if (reloadTimes++ < 20)
                    getConfig()
                TBAHelper.updatePoints(
                    EventPoints.filec_cloak_get, mutableMapOf(
                        EventPoints.Time to System.currentTimeMillis() / 1000 - startSecond,
                        EventPoints.getsuccess to false,
                        EventPoints.source to cloakState
                    )
                )
            })
    }

    private fun getConfig(): String {
        return StringBuilder().apply {
            mutableMapOf(
                CloakKey.sofia to getAndroidId().encode(),
                CloakKey.fusty to System.currentTimeMillis().toString().encode(),
                CloakKey.stubby to Build.MODEL.encode(),
//            CloakKey.noodle to BuildConfig.APPLICATION_ID.encode(),
                CloakKey.noodle to BuildConfig.APPLICATION_ID.encode(),
                CloakKey.ohio to Build.VERSION.RELEASE.encode(),
//            CloakKey.previous to "",
//
                CloakKey.abbey to getAndroidId().encode(),
                CloakKey.animist to "twigging".encode(),
////            CloakKey.bell to  "",
                CloakKey.rib to BuildConfig.VERSION_NAME.encode(),
////            CloakKey.crib to "",
////            CloakKey.stab to "",
////            CloakKey.gondola to "",
                CloakKey.tabletop to MMKVHelper.GaId
            ).forEach {
                this.append("&${it.key}=${it.value}")
            }
        }.toString().substring(1)

    }

}