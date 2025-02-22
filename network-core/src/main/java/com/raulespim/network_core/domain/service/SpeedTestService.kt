package com.raulespim.network_core.domain.service

interface SpeedTestService {
    suspend fun measureDownloadSpeed(): Double
    suspend fun measureUploadSpeed(): Double
}