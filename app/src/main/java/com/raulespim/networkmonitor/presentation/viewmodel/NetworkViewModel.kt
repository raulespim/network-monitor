package com.raulespim.networkmonitor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raulespim.network_core.domain.model.NetworkStatus
import com.raulespim.network_core.domain.usecase.MeasureConnectionSpeedUseCase
import com.raulespim.network_core.domain.usecase.ObserveNetworkStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NetworkViewModel @Inject constructor(
    private val observeStatusUseCase: ObserveNetworkStatusUseCase,
    private val measureSpeedUseCase: MeasureConnectionSpeedUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(NetworkUiState())
    val uiState: StateFlow<NetworkUiState> = _uiState.asStateFlow()

    init {
        observeNetworkStatus()
    }

    fun observeNetworkStatus() {
        viewModelScope.launch {
            observeStatusUseCase.execute(Unit).collect { status ->
                _uiState.update { it.copy(
                    networkStatus = status,
                    isLoading = status is NetworkStatus.Connected && !it.hasSpeedData
                ) }

                if (status is NetworkStatus.Connected) {
                    measureConnectionSpeed()
                }
            }
        }
    }

    fun measureConnectionSpeed() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            measureSpeedUseCase.execute(Unit).collect { result ->
                _uiState.update {
                    when (result) {
                        is NetworkStatus.SpeedMeasure -> it.copy(
                            downloadSpeed = result.download,
                            uploadSpeed = result.upload,
                            isLoading = false,
                            hasSpeedData = true
                        )
                        is NetworkStatus.Error -> it.copy(
                            errorMessage = result.cause,
                            isLoading = false
                        )
                        else -> it
                    }
                }
            }
        }
    }

    fun dismissError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}

data class NetworkUiState(
    val networkStatus: NetworkStatus = NetworkStatus.Disconnected,
    val downloadSpeed: Double = 0.0,
    val uploadSpeed: Double = 0.0,
    val isLoading: Boolean = false,
    val hasSpeedData: Boolean = false,
    val errorMessage: String? = null
)