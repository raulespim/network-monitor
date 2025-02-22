package com.raulespim.network_core.security

import com.raulespim.network_core.security.SecurityConfig.CERTIFICATE_PINNER
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLHandshakeException
import javax.net.ssl.X509TrustManager

object SecurityConfig {
    const val TLS_VERSION = "TLSv1.3"
    private val trustManager = TrustManagerProxy()

    private val sslContext by lazy {
        SSLContext.getInstance(TLS_VERSION).apply {
            init(null, arrayOf(trustManager), SecureRandom())
        }
    }

    val CERTIFICATE_PINNER = CertificatePinner.Builder()
        .add("srv.openspeedtest.com", "sha256/YLWMhBqO1+E8L+zWZtnjNdygZJwH+HFmE4J/juAGWkg=")
        .build()

    fun createSecureOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .certificatePinner(CERTIFICATE_PINNER)
            .sslSocketFactory(sslContext.socketFactory, trustManager)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .callTimeout(60, TimeUnit.SECONDS)
            .build()
    }
}

private class TrustManagerProxy : X509TrustManager {
    override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
        // Client certificate validation (not needed for server verification)
    }
    override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
        if (CERTIFICATE_PINNER.findCertificateChainPins(chain).isEmpty()) {
            throw SSLHandshakeException("Certificate pinning verification failed")
        }
    }
    override fun getAcceptedIssuers() = arrayOf<X509Certificate>()
}