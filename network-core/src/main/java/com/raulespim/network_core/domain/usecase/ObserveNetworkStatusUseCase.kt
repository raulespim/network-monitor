package com.raulespim.network_core.domain.usecase

import com.raulespim.network_core.domain.model.NetworkStatus
import com.raulespim.network_core.domain.repository.NetworkRepository
import javax.inject.Inject

class ObserveNetworkStatusUseCase @Inject constructor(
    private val repository: NetworkRepository
) : BaseNetworkUseCase<Unit, NetworkStatus>() {
    override suspend fun execute(params: Unit) = repository.networkStatus
}