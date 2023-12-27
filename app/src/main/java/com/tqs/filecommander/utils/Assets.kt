package com.tqs.filecommander.utils

import android.content.Context


fun getJsonFromAssets(context: Context, fileName: String): String {
    var json = ""
    try {
        val stream = context.assets.open(fileName)
        val size = stream.available()
        val buffer = ByteArray(size)
        stream.read(buffer)
        stream.close()
        json = String(buffer, Charsets.UTF_8)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return json
}
