package com.tqs.filecommander.utils

import android.content.Context

object PixesUtils {
    fun px2dp(context: Context, px: Int): Int {
        return 0
    }

    fun dp2px(context: Context, dp: Int): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density).toInt()
    }
}