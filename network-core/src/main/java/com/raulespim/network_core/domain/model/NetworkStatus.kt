package com.raulespim.network_core.domain.model

sealed class NetworkStatus {
    data object Connected : NetworkStatus()
    data object Disconnected : NetworkStatus()
    data class Error(val cause: String) : NetworkStatus()
    data class SpeedMeasure(val download: Double, val upload: Double) : NetworkStatus()
}