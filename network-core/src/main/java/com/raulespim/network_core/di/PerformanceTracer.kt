package com.raulespim.network_core.di

import com.raulespim.network_core.BuildConfig
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

interface PerformanceTracer {
    suspend fun <T> traceSpeedTest(block: suspend () -> T): T
    fun logMeasurement(metricName: String, value: Double)
}

class PerformanceTracerImpl @Inject constructor() : PerformanceTracer {
    override suspend fun <T> traceSpeedTest(block: suspend () -> T): T {
        val startTime = System.nanoTime()
        val result = block()
        val duration = (System.nanoTime() - startTime) / 1e6
        logMeasurement("speed_test_duration_ms", duration)
        return result
    }

    override fun logMeasurement(metricName: String, value: Double) {
        if (BuildConfig.DEBUG) {
            Timber.d("$metricName: $value")
        }
    }
}