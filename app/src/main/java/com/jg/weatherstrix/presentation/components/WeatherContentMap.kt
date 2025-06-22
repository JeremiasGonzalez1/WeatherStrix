package com.jg.weatherstrix.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.jg.weatherstrix.presentation.screen.MapUIState

@Composable
fun WeatherMapScreen(
    weatherState: MapUIState,
    activeLocation: LatLng?,
    cameraPositionState: CameraPositionState,
    onMapClick: (LatLng) -> Unit,
    onBackToUserLocation: () -> Unit,
    onRequestPermission: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // El mapa es la base, siempre visible
        GoogleMapView(
            cameraPositionState = cameraPositionState,
            userLocation = activeLocation,
            updateLocation = onMapClick
        )

        // Capas de UI que se renderizan encima del mapa
        when (weatherState) {
            is MapUIState.Loading -> {
                ShimmerAnimation(Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
                    .width(280.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(16.dp)
                    ).background(
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.50f),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .height(335.dp))
            }
            is MapUIState.PermissionDenied -> {
                PermissionDeniedScreen(onRequestPermission = onRequestPermission)
            }
            is MapUIState.Error -> {
                ErrorScreen(message = weatherState.messageError)
            }
            is MapUIState.Success -> {
                WeatherInfoPanel(
                    weather = weatherState.weather,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp)
                        .width(280.dp)
                        ,
                    actions = {
                        IconButton(onClick = { /* TODO: funcionalidad de favorito */ }) {
                            Icon(
                                imageVector = Icons.Default.FavoriteBorder,
                                contentDescription = "Agregar a favoritos"
                            )
                        }
                        IconButton(onClick = onBackToUserLocation) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Volver a mi ubicación"
                            )
                        }
                    }
                )
            }
        }
    }
}