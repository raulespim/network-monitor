package com.raulespim.network_core.security

import android.util.Base64
import okhttp3.CertificatePinner
import java.security.MessageDigest
import java.security.PublicKey
import java.security.cert.X509Certificate

fun CertificatePinner.findCertificateChainPins(chain: Array<X509Certificate>): List<String> {
    return try {
        chain.mapNotNull { certificate ->
            val pin = certificate.publicKey.toSha256Pin()
            findMatchingPins(certificate).firstOrNull { it == pin }
        }
    } catch (e: Exception) {
        emptyList()
    }
}

private fun CertificatePinner.findMatchingPins(certificate: X509Certificate): List<String> {
    val hostname = "api.example.com" // Replace with real host
    return pins.filter { pin -> pin.pattern == hostname }.map { it.hash.base64() }
}

private fun PublicKey.toSha256Pin(): String {
    val keyBytes = this.encoded
    val digest = MessageDigest.getInstance("SHA-256").digest(keyBytes)
    return "sha256/${Base64.encodeToString(digest, Base64.NO_WRAP)}"
}