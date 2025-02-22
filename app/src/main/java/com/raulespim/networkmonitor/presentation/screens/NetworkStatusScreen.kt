package com.raulespim.networkmonitor.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.raulespim.network_core.domain.model.NetworkStatus
import com.raulespim.networkmonitor.common.composable.ErrorSnackbar
import com.raulespim.networkmonitor.presentation.viewmodel.NetworkUiState
import com.raulespim.networkmonitor.presentation.viewmodel.NetworkViewModel
import com.raulespim.networkmonitor.ui.theme.AppTheme

@Composable
fun NetworkStatusScreen(viewModel: NetworkViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        if (uiState.networkStatus is NetworkStatus.Disconnected) {
            viewModel.observeNetworkStatus()
        }
    }

    NetworkStatusContent(
        uiState = uiState,
        onRetry = { viewModel.measureConnectionSpeed() },
        onDismissError = { viewModel.dismissError() }
    )
}

@Composable
private fun NetworkStatusContent(
    uiState: NetworkUiState,
    onRetry: () -> Unit,
    onDismissError: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            NetworkStatusIndicator(uiState.networkStatus)

            if (uiState.networkStatus is NetworkStatus.Connected) {
                ConnectionSpeedDisplay(
                    downloadSpeed = uiState.downloadSpeed,
                    uploadSpeed = uiState.uploadSpeed,
                    isLoading = uiState.isLoading,
                    onRetry = onRetry
                )
            }
        }

        uiState.errorMessage?.let { message ->
            ErrorSnackbar(
                message = message,
                onDismiss = onDismissError,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
private fun NetworkStatusIndicator(status: NetworkStatus) {
    val (icon, color, text) = when (status) {
        is NetworkStatus.Connected -> Triple(Icons.Default.CheckCircle, Color.Green, "Connected")
        is NetworkStatus.Disconnected -> Triple(Icons.Default.Close, Color.Red, "No Connection")
        is NetworkStatus.Error -> Triple(Icons.Default.Warning, Color.Yellow, "Connection Error")
        else -> Triple(Icons.Default.Refresh, Color.Gray, "Unknown")
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(48.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge,
            color = color
        )
    }
}

@Composable
private fun ConnectionSpeedDisplay(
    downloadSpeed: Double,
    uploadSpeed: Double,
    isLoading: Boolean,
    onRetry: () -> Unit
) {
    Card(elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Connection Speed",
                style = MaterialTheme.typography.titleMedium
            )

            AnimatedVisibility(visible = isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }

            AnimatedVisibility(visible = !isLoading) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    SpeedIndicator("Download", downloadSpeed)
                    SpeedIndicator("Upload", uploadSpeed)
                    Button(
                        onClick = onRetry,
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("Refresh Speeds")
                    }
                }
            }
        }
    }
}

@Composable
private fun SpeedIndicator(label: String, speed: Double) {
    Row(
        modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("$label:", modifier = Modifier.width(100.dp))
        Text(
            text = "${"%.2f".format(speed)} Mbps",
            style = MaterialTheme.typography.bodyLarge.copy(fontFamily = FontFamily.Monospace)
        )
    }
}

@Preview
@Composable
fun NetworkStatusScreenPreview() {
    AppTheme {
        Surface {
            Box(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    NetworkStatusContent(
                        uiState = NetworkUiState(
                            networkStatus = NetworkStatus.Connected,
                            downloadSpeed = 10.0,
                            uploadSpeed = 5.0,
                            isLoading = false
                        ),
                        onRetry = {},
                        onDismissError = {}
                    )
                }
            }

        }
    }
}