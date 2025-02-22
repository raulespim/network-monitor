package com.raulespim.network_core.data.service

import com.raulespim.network_core.di.PerformanceTracer
import com.raulespim.network_core.domain.service.SpeedTestService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.net.SocketTimeoutException

class SpeedTestServiceImpl(
    private val client: OkHttpClient,
    private val tracer: PerformanceTracer
) : SpeedTestService {

    private companion object {
        const val TEST_DOWNLOAD_URL = "https://srv.openspeedtest.com:8000/download?nocache=1"
        const val TEST_UPLOAD_URL = "https://srv.openspeedtest.com:8000/upload?nocache=1"
        const val TEST_FILE_SIZE_BYTES = 10_000_000
        const val TIMEOUT_SECONDS = 10L
    }

    override suspend fun measureDownloadSpeed(): Double {
        return tracer.traceSpeedTest {
            try {
                val request = Request.Builder().url(TEST_DOWNLOAD_URL).build()
                val startTime = System.nanoTime()

                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    response.body?.bytes()

                    val elapsedSeconds = (System.nanoTime() - startTime) / 1e9
                    (TEST_FILE_SIZE_BYTES * 8) / elapsedSeconds / 1e6 // Mbps
                }
            } catch (e: SocketTimeoutException) {
                throw SpeedTestException("Download timed out", e)
            } catch (e: IOException) {
                throw SpeedTestException("Erro de rede", e)
            }
        }
    }

    override suspend fun measureUploadSpeed(): Double {
        return tracer.traceSpeedTest {
            val testData = ByteArray(TEST_FILE_SIZE_BYTES) { 0x1 }
            val requestBody = testData.toRequestBody("application/octet-stream".toMediaType())

            val request = Request.Builder()
                .url(TEST_UPLOAD_URL)
                .post(requestBody)
                .build()

            val startTime = System.nanoTime()
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Upload failed $response")
                val elapsedSeconds = (System.nanoTime() - startTime) / 1e9
                (TEST_FILE_SIZE_BYTES * 8) / elapsedSeconds / 1e6 // Mbps
            }
        }
    }
}

class SpeedTestException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause)