package com.raulespim.network_core.domain.usecase

import kotlinx.coroutines.flow.Flow

abstract class BaseNetworkUseCase<in P, out T> {
    abstract suspend fun execute(params: P): Flow<T>
}