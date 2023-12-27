package com.tqs.filecommander.utils

import android.content.Context
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import com.tqs.filecommander.BuildConfig
import java.net.URLEncoder

fun String?.toast(context: Context) {
    if (BuildConfig.DEBUG) {
        Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
    }
}

fun String.logE(){
    if (BuildConfig.DEBUG){
        Log.e("File Commander",this)
    }
}
fun String.logW(){
    if (BuildConfig.DEBUG){
        Log.w("File Commander",this)
    }
}
fun String.logD(){
    if (BuildConfig.DEBUG){
        Log.d("File Commander",this)
    }
}

fun String.logI(){
    if (BuildConfig.DEBUG){
        Log.i("File Commander",this)
    }
}

fun String?.encode():String{
    return URLEncoder.encode(this, "UTF-8")
}

