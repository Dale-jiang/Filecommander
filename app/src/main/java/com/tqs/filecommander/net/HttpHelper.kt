package com.tqs.filecommander.net

import com.tqs.filecommander.BuildConfig
import com.tqs.filecommander.utils.logE
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread


const val DEFAULT_CONNECT_TIME = 10L
const val DEFAULT_WRITE_TIME = 30L
const val DEFAULT_READ_TIME = 30L

object HttpHelper {
    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(DEFAULT_CONNECT_TIME, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_WRITE_TIME, TimeUnit.SECONDS)
            .readTimeout(DEFAULT_READ_TIME, TimeUnit.SECONDS)
            .build()
    }
    private val mediaType = MediaType.parse("application/json")

    fun sendRequestPost(
        baseUrl: String = BuildConfig.TBAUrl,
        jsonObject: JSONObject,
        resultSuccess: (String) -> Unit,
        resultFailed: (Int, String) -> Unit
    ) {
        runCatching {
            val requestString = jsonObject.toString()
            "HttpHelper ${baseUrl} POST json = $requestString".logE()
            okHttpClient.newCall(
                Request.Builder().url(baseUrl)?.post(RequestBody.create(mediaType, requestString))?.build()
            ).execute().let {
                if (it.isSuccessful) {
                    it.body().string().let { message ->
                        "HttpHelper ${baseUrl} POST code = ${it.code()}  message = ${message}".logE()
                        resultSuccess.invoke(message)
                    }
                } else {
                    "HttpHelper ${baseUrl} POST code = ${it.code()}  message = ${it.message()}".logE()
                    resultFailed.invoke(it.code(), it.message())
                }
            }


        }
    }

    fun sendRequestGet(
        baseUrl: String = BuildConfig.CloakUrl,
        requestString: String,
        resultSuccess: (String) -> Unit,
        resultFailed: (Int, String) -> Unit
    ) {

        runCatching {
            "HttpHelper ${baseUrl} GET requestString = $requestString".logE()
            val request = Request.Builder().url("${baseUrl}?${requestString}")?.build()
            okHttpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call?, e: IOException?) {
                    "HttpHelper ${baseUrl} GET code = 500  message = ${e?.message}".logE()
                    resultFailed.invoke(500, e?.message ?: "")
                }

                override fun onResponse(call: Call?, it: Response?) {
                    if (it?.isSuccessful == true) {
                        it.body().string().let { message ->
                            "HttpHelper ${baseUrl} GET code = ${it.code()}  message = ${message}".logE()
                            resultSuccess.invoke(message)
                        }
                        it.body().close()
                    } else {
                        "HttpHelper ${baseUrl} GET code = ${it?.code()}  message = ${it?.message()}".logE()
                        resultFailed.invoke(it?.code() ?: 500, it?.message() ?: "")
                    }
                }

            })
        }

    }

}















