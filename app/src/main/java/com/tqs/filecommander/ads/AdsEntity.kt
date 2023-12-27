package com.tqs.filecommander.ads

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class AdsEntity(
    @SerializedName("ujn")
    var showMax: Int = 40,
    @SerializedName("koof")
    var clickMax: Int = 15,
    @SerializedName("fc_launch")
    var adsFullScreen: MutableList<AdsItem>? = null,
    @SerializedName("fc_scan_int")
    var adsInsertResultScan: MutableList<AdsItem>? = null,
    @SerializedName("fc_scan_nat")
    var adsNativeResultScan: MutableList<AdsItem>? = null
) : Parcelable

@Keep
@Parcelize
data class AdsItem(
    @SerializedName("popl")
    var adsId: String,
    @SerializedName("gbne")
    var adsPlatform: String,
    @SerializedName("vtha")
    var adsType: String,
    @SerializedName("cgabk")
    var adsAliveMillis: Int,
    @SerializedName("buy")
    var adsWeight: Int
) : Parcelable

@Keep
@Parcelize
data class AdsUserCost(
    @SerializedName("adltv_top10")
    var top10: Double = 1.0,
    @SerializedName("adltv_top20")
    var top20: Double = 0.8,
    @SerializedName("adltv_top30")
    var top30: Double = 0.6,
    @SerializedName("adltv_top40")
    var top40: Double = 0.5,
    @SerializedName("adltv_top50")
    var top50: Double = 0.1,
) : Parcelable

@Keep
@Parcelize
data class AdsCount(
    var time: Long = System.currentTimeMillis(),
    var count: Int = 1
) : Parcelable

@Keep
enum class AdsItemType(val adsItemType: String) {
    ADSFULLSCREEN("fc_launch"),
    ADSINSERTRESULTSCAN("fc_scan_int"),
    ADSNATIVERESULTSCAN("fc_scan_nat")
}

object ADSType {
    const val ADS_TYPE_OP = "op"
    const val ADS_TYPE_INT = "int"
    const val ADS_TYPE_NAT = "nat"
}

object ADSPlatform {
    const val ADS_PLAT_ADMOB = "admob"
    const val ADS_PLAT_MAX = "max"
    const val ADS_PLAT_TOPON = "topon"
    const val ADS_PLAT_TRADPLUS = "tradplus"
}

object ADLTV {
    const val AdLTV_Top10Percent = "AdLTV_Top10Percent"
    const val AdLTV_Top20Percent = "AdLTV_Top20Percent"
    const val AdLTV_Top30Percent = "AdLTV_Top30Percent"
    const val AdLTV_Top40Percent = "AdLTV_Top40Percent"
    const val AdLTV_Top50Percent = "AdLTV_Top50Percent"
}