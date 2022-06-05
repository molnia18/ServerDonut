package com.example.authentication

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import io.ktor.util.*

private val hashKey = System.getenv("HASH_SECRET_KEY").toByteArray()
private val hmacKey = SecretKeySpec(hashKey, "HmacSHA1")

fun hash(password:String):String {
    val hmac = Mac.getInstance("HmacSHA1")
    hmac.init(hmacKey)
    return hex(hmac.doFinal(password.toByteArray(Charsets.UTF_8)))
}