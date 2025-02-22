package com.raulespim.network_core.domain.usecase

import com.raulespim.network_core.di.PerformanceTracer
import com.raulespim.network_core.domain.model.NetworkStatus
import com.raulespim.network_core.domain.repository.NetworkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MeasureConnectionSpeedUseCase @Inject constructor(
    private val repository: NetworkRepository,
    private val performanceTracer: PerformanceTracer
) : BaseNetworkUseCase<Unit, NetworkStatus>() {

    override suspend fun execute(params: Unit): Flow<NetworkStatus> {
        return flow {
            performanceTracer.traceSpeedTest {
                repository.measureConnectionSpeed().collect { status ->
                    when (status) {
                        is NetworkStatus.SpeedMeasure -> {
                            performanceTracer.logMeasurement("download_speed", status.download)
                            performanceTracer.logMeasurement("upload_speed", status.upload)
                        }
                        else -> {/* Handle other cases */}
                    }
                    emit(status)
                }
            }
        }
    }
}