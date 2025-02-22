package com.raulespim.network_core.domain.repository

import com.raulespim.network_core.domain.model.NetworkStatus
import kotlinx.coroutines.flow.Flow

interface NetworkRepository {
    val networkStatus: Flow<NetworkStatus>
    suspend fun measureConnectionSpeed(): Flow<NetworkStatus>
}