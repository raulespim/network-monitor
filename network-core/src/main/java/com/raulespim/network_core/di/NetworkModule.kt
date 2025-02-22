package com.raulespim.network_core.di

import android.content.Context
import android.net.ConnectivityManager
import com.raulespim.network_core.data.repository.NetworkRepositoryImpl
import com.raulespim.network_core.data.service.SpeedTestServiceImpl
import com.raulespim.network_core.domain.repository.NetworkRepository
import com.raulespim.network_core.domain.service.SpeedTestService
import com.raulespim.network_core.security.SecurityConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideConnectivityManager(
        @ApplicationContext context: Context
    ): ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = SecurityConfig.createSecureOkHttpClient()

    @Provides
    @Singleton
    fun provideSpeedTestService(client: OkHttpClient, tracer: PerformanceTracer): SpeedTestService =
        SpeedTestServiceImpl(client, tracer)

    @Provides
    @Singleton
    fun providePerformanceTracer(): PerformanceTracer = PerformanceTracerImpl()

    @Provides
    @Singleton
    fun provideNetworkRepository(
        connectivityManager: ConnectivityManager,
        speedTestService: SpeedTestService
    ): NetworkRepository = NetworkRepositoryImpl(connectivityManager, speedTestService)
}