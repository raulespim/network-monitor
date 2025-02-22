package com.raulespim.networkmonitor.di

import android.os.StrictMode
import com.raulespim.network_core.di.PerformanceTracer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PerformanceModule {

//    @Provides
//    fun provideNetworkMonitor(): PerformanceTracer = object : PerformanceTracer {
//        override suspend fun <T> traceSpeedTest(block: suspend () -> T): T {
//            StrictMode.noteSlowCall("Speed test execution")
//            return block()
//        }
//
//        override fun logMeasurement(metricName: String, value: Double) {
//           // TODO("Not yet implemented")
//        }
//    }
}