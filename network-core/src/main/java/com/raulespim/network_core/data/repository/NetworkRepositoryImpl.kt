package com.raulespim.network_core.data.repository

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.raulespim.network_core.domain.model.NetworkStatus
import com.raulespim.network_core.domain.repository.NetworkRepository
import com.raulespim.network_core.domain.service.SpeedTestService
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NetworkRepositoryImpl @Inject constructor(
    private val connectivityManager: ConnectivityManager,
    private val speedTestService: SpeedTestService
) : NetworkRepository {

    private val _networkStatus = MutableStateFlow<NetworkStatus>(NetworkStatus.Disconnected)

    override val networkStatus: Flow<NetworkStatus> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(NetworkStatus.Connected)
            }

            override fun onLost(network: Network) {
                trySend(NetworkStatus.Disconnected)
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(request, callback)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }.combine(_networkStatus) { realtime, cached -> realtime }

    override suspend fun measureConnectionSpeed(): Flow<NetworkStatus> = flow {
        try {
            val downloadSpeed = speedTestService.measureDownloadSpeed()
            val uploadSpeed = speedTestService.measureUploadSpeed()
            emit(NetworkStatus.SpeedMeasure(downloadSpeed, uploadSpeed))
        } catch (e: Exception) {
            emit(NetworkStatus.Error("Speed measurement failed: ${e.localizedMessage}"))
        }
    }
}