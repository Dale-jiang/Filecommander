package com.tqs.filecommander.notification

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class NotificationEntity(
    @SerializedName("fcpop_switch")
    var notificationSwitch: Int = 0,
    @SerializedName("fcreffer")
    var referrerSwitch: Int = 0,
    @SerializedName("fc_t")
    var timing: NotificationItem,
    @SerializedName("fc_unl")
    var unclock: NotificationItem,
    @SerializedName("fc_char")
    var battery: NotificationItem,
    @SerializedName("fc_uni")
    var uninstall: NotificationItem,
) : Parcelable

@Keep
@Parcelize
data class NotificationItem(
    @SerializedName("open")
    var isPopup: Int = 1,
    @SerializedName("f")
    var delayPopupTime: Int = 5,
    @SerializedName("limit")
    var dayShowLimit: Int = 100,
    @SerializedName("interval")
    var intervalPopupTime: Int = 10
) : Parcelable

@Keep
@Parcelize
data class NotificationConfig(
    var scenes: MutableList<ConfigItem> = mutableListOf()
) : Parcelable

@Keep
@Parcelize
data class ConfigItem(
    var name: String = "",
    var notification: String = ""
) : Parcelable

@Keep
@Parcelize
data class NotificationShowed(
    var lastShowTime: Long = System.currentTimeMillis(),
    var showTimes: Int = 0,
    var title: String = "",
    var content: String = ""
) : Parcelable


object NotificationKey {
    // from notification config json
    const val SCHEDULED = "fc_t"
    const val UNCLOCK = "fc_unl"
    const val CHARGE = "fc_char"
    const val UNINSTALL = "fc_uni"
    // from showed notification config
    const val Scheduled = "Scheduled"
    const val Unlock = "Unlock"
    const val Charge = "Charge"
    const val Uninstall = "Uninstall"

    const val notifyType = "notifyType"
}
