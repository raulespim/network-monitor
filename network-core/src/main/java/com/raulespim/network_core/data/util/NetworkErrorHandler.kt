package com.raulespim.network_core.data.util

import com.raulespim.network_core.domain.model.NetworkStatus
import java.io.IOException

object NetworkErrorHandler {
    fun handleError(throwable: Throwable): NetworkStatus.Error {
        return when (throwable) {
            is IOException -> NetworkStatus.Error("Network error: ${throwable.message}")
            is SecurityException -> NetworkStatus.Error("Security violation: ${throwable.message}")
            else -> NetworkStatus.Error("Unexpected error: ${throwable.message}")
        }
    }
}