package com.jg.weatherstrix.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.jg.weatherstrix.presentation.screen.MapUIState

@Composable
fun WeatherPanelContainer(
    weatherState: MapUIState,
    onRequestPermission: () -> Unit,
    saveLocation: () -> Unit,
    onHidePanel: () -> Unit,
) {
    when (weatherState) {
        is MapUIState.Success -> {
            WeatherInfoPanel(
                weather = weatherState.weather,
                modifier = Modifier.width(280.dp),
                onClose = onHidePanel,
                onFavorite = saveLocation,
                isFavorite = false
            )
        }
        is MapUIState.Loading -> {
            ShimmerAnimation(
                modifier = Modifier
                    .padding(16.dp)
                    .width(280.dp)
                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp))
                    .background(
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.50f),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .height(335.dp)
            )
        }
        is MapUIState.Error -> {
            OfflinePanel(
                message = weatherState.messageError,
            )
        }
        is MapUIState.PermissionDenied -> {
            PermissionDeniedScreen(onRequestPermission = onRequestPermission)
        }
        else -> {}
    }
}

@Composable
fun OfflinePanel(
    message: String,
) {
    Column(modifier = Modifier.width(280.dp).padding(16.dp)) {
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
} 