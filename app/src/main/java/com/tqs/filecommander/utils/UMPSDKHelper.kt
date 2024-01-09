package com.tqs.filecommander.utils

import android.util.Log
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentForm
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import com.tqs.filecommander.base.BaseActivity
import com.tqs.filecommander.mmkv.MMKVHelper.authorization
import com.tqs.filecommander.mmkv.MMKVHelper.authorizationTime


object UMPSDKHelper {

    private var consentInformation: ConsentInformation? = null
    private var isUseTestDevice = false
    fun initUMPSDK(context: BaseActivity<*, *>, callBack: (Boolean) -> Unit) {

        if (authorization && (System.currentTimeMillis() - authorizationTime < 380 * 24 * 60 * 60 * 1000L)) {
            callBack.invoke(true)
            return
        }


        val paramsBuilder = ConsentRequestParameters
            .Builder()

        if (isUseTestDevice) {
            val debugSettings = ConsentDebugSettings.Builder(context)
                .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
                .addTestDeviceHashedId("TEST-DEVICE-HASHED-ID")
                .build()

            paramsBuilder.setConsentDebugSettings(debugSettings)
        }

        val params = paramsBuilder.setTagForUnderAgeOfConsent(false)
            .build()

        consentInformation = UserMessagingPlatform.getConsentInformation(context)
        consentInformation?.requestConsentInfoUpdate(context,
            params,
            {
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                    context,
                    ConsentForm.OnConsentFormDismissedListener { loadAndShowError ->
                        // Consent gathering failed.

                        if (loadAndShowError != null || consentInformation?.canRequestAds() == false) {
                            callBack.invoke(false)

                            Log.w(
                                "", String.format(
                                    "%s: %s",
                                    loadAndShowError?.errorCode,
                                    loadAndShowError?.message
                                )
                            )

                            return@OnConsentFormDismissedListener
                        }

                        // Consent has been gathered.
                        if (consentInformation?.canRequestAds() == true) {
                            authorization = true
                            authorizationTime =System.currentTimeMillis()
                            callBack.invoke(true)
                        }
                    }
                )
            },
            { requestConsentError ->
                // Consent gathering failed.
                callBack.invoke(false)
                Log.w(
                    "", String.format(
                        "%s: %s",
                        requestConsentError.errorCode,
                        requestConsentError.message
                    )
                )
            })

    }

}