package com.tqs.filecommander.tba

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.telephony.TelephonyManager
import android.webkit.WebSettings
import com.tqs.filecommander.BuildConfig
import com.tqs.filecommander.cloak.CloakKey
import com.tqs.filecommander.mmkv.MMKVHelper
import com.tqs.filecommander.utils.application
import com.tqs.filecommander.utils.getAndroidId
import org.json.JSONObject
import java.util.Locale
import java.util.UUID

fun getRequestJson(function: () -> MutableMap<String, Any> = { mutableMapOf<String, Any>() }): JSONObject =
    JSONObject().apply {
        put(EventCommon.celsius, getCelsius())
        put(EventCommon.quixotic, getQuixotic())
        put(EventCommon.filigree, getFiligree())
        function.invoke().forEach { (key, value) ->
            put(key, value)
        }

    }


private fun getCelsius(): JSONObject = JSONObject().apply {
    put(EventCelsius.ontogeny, Locale.getDefault().language)
    put(EventCelsius.animist, "twigging")
    put(EventCelsius.rib, BuildConfig.VERSION_NAME)
//        put(EventCelsius.crib, "")
    put(EventCelsius.noodle, BuildConfig.APPLICATION_ID)
//        put(EventCelsius.previous, "")
}

@SuppressLint("HardwareIds")
private fun getQuixotic(): JSONObject = JSONObject().apply {
    put(EventQuixotic.abbey, getAndroidId())
//        put(EventQuixotic.nepotism, "")
//        put(EventQuixotic.alasdair, "")
    put(EventQuixotic.sofia, getAndroidId())
    put(EventQuixotic.fusty, System.currentTimeMillis().toString())
    put(EventQuixotic.weight, Locale.getDefault().language)
//        put(EventQuixotic.stab, "")
    put(EventQuixotic.fairfax, (application.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).simOperator)
    put(EventQuixotic.ohio, Build.VERSION.RELEASE)
//        put(EventQuixotic.freeze, "")
}

private fun getFiligree(): JSONObject = JSONObject().apply {
    put(EventFiligree.invent, application.resources.displayMetrics.let { "${it.widthPixels}*${it.heightPixels}" })
    put(EventFiligree.nulls, "")
//        put(EventFiligree.bell, "")
//        put(EventFiligree.forbear, "","UTF-8"))

    put(CloakKey.tabletop, MMKVHelper.GaId)
    put(EventFiligree.leaf, Build.MANUFACTURER)
    put(EventFiligree.benefit, UUID.randomUUID().toString())
    put(EventFiligree.stubby, Build.MODEL)
}

fun getEventInstall(): JSONObject = JSONObject().apply {
    put(EventInstall.squawk, "build/${Build.ID}")
    put(EventInstall.omnibus, MMKVHelper.installReferrer)
    put(EventInstall.booty, MMKVHelper.installReferrerVersion)
    put(EventInstall.surname, WebSettings.getDefaultUserAgent(application))
    put(EventInstall.hades, if (MMKVHelper.isLimitAdTrackingEnabled) "enough" else "shrub")
    put(EventInstall.cacao, MMKVHelper.referrerClickTimestampSeconds)
    put(EventInstall.dough, MMKVHelper.installBeginTimestampSeconds)
    put(EventInstall.chunky, MMKVHelper.referrerClickTimestampServerSeconds)
    put(EventInstall.Is, MMKVHelper.installBeginTimestampServerSeconds)
    put(EventInstall.hoy, TBAHelper.firstInstall)
    put(EventInstall.fury, TBAHelper.lastUpdate)
    put(EventInstall.smallish, MMKVHelper.googlePlayInstantParam)
}

fun getEventSession(): JSONObject = JSONObject().apply { }


fun getEventAdvertising(
    adsType: String,
    adsId: String,
    adsPlat: String = "admob",
    adsSDK: String,
    adsIndex: String
): MutableMap<String, String> =
    mutableMapOf<String, String>().apply {
//    put(EventAdvertising.blink, "")
//    put(EventAdvertising.sank, "")
        put(EventAdvertising.monetary, adsPlat)
        put(EventAdvertising.monomer, adsSDK)
        put(EventAdvertising.upland, adsId)
        put(EventAdvertising.sex, adsIndex)
//    put(EventAdvertising.exceed, "")
//    put(EventAdvertising.millikan, "")
        put(EventAdvertising.bolo, adsType)
//    put(EventAdvertising.pencil, "")
//    put(EventAdvertising.wholly, "")
//    put(EventAdvertising.abstract, "")
    }

fun getEventPoints(map: MutableMap<String, Any?>): JSONObject = JSONObject().apply {
    map.forEach { (key, value) ->
        put(key, value)
    }
}
