package com.tqs.filecommander.net

import com.blankj.utilcode.util.EncodeUtils
import org.json.JSONObject
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object SecretHelper {
    const val key = "9WrCi7bgchnBqFPVkN0nXg=="
    const val cipherMode = "AES/ECB/PKCS5Padding"
    fun encryptJson(jsonObject: JSONObject): String? {
        runCatching {
            return EncodeUtils.base64Encode2String(Cipher.getInstance(cipherMode).let {
                it.init(Cipher.ENCRYPT_MODE, SecretKeySpec(EncodeUtils.base64Decode(key), "AES"))
                it.doFinal(jsonObject.toString().toByteArray())
            })
        }
        return null
    }
}