package com.tqs.filecommander.utils

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever

object VideoUtils {
    fun getBitmap(path: String): Bitmap? {
        val mmr = MediaMetadataRetriever()
        mmr.setDataSource(path)
        val bitmap = mmr.frameAtTime
        mmr.release()
        return bitmap
    }
}