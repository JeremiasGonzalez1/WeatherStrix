package com.jg.weatherstrix.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.jg.weatherstrix.domain.models.Weather
import com.jg.weatherstrix.presentation.screen.MapUIState
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.shadow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherMapScreen(
    weatherState: MapUIState,
    activeLocation: LatLng?,
    cameraPositionState: CameraPositionState,
    onMapClick: (LatLng) -> Unit,
    onBackToUserLocation: () -> Unit,
    onRequestPermission: () -> Unit,
    saveLocation: () -> Unit = {},
    favorites: List<Weather> = emptyList(),
    onSelectFavorite: (Weather) -> Unit = {},
    onDeleteFavorite: (Weather) -> Unit = {}
) {
    var panelVisible by remember { mutableStateOf(true) }
    var favoritesVisible by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMapView(
            cameraPositionState = cameraPositionState,
            userLocation = activeLocation,
            updateLocation = onMapClick
        )

        Column(
            Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            if (panelVisible) {
                when (weatherState) {
                    is MapUIState.Success -> {
                        WeatherInfoPanel(
                            weather = weatherState.weather,
                            modifier = Modifier.width(280.dp),
                            actions = {
                                IconButton(onClick = { panelVisible = false }) {
                                    Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Minimizar")
                                }
                                IconButton(onClick = saveLocation) {
                                    Icon(Icons.Default.FavoriteBorder, contentDescription = "Guardar favorito")
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
                    is MapUIState.Loading -> {
                        ShimmerAnimation(modifier = Modifier
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
                    is MapUIState.Error -> {
                        ErrorScreen(message = weatherState.messageError)
                    }
                    is MapUIState.PermissionDenied -> {
                        PermissionDeniedScreen(onRequestPermission = onRequestPermission)
                    }
                    else -> {}
                }
            } else {
                Card(
                    modifier = Modifier
                        .width(48.dp)
                        .height(48.dp)
                        .clickable { panelVisible = true },
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Icon(
                        Icons.Default.KeyboardArrowRight,
                        contentDescription = "Mostrar panel",
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }

            Spacer(Modifier.height(8.dp))
            Card(
                modifier = Modifier
                    .width(48.dp)
                    .height(48.dp)
                    .clickable { favoritesVisible = true },
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Icon(
                    Icons.Default.FavoriteBorder,
                    contentDescription = "Favoritos",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }

        if (favoritesVisible) {
            ModalBottomSheet(onDismissRequest = { favoritesVisible = false }) {
                Column {
                    Text("Favoritos", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(16.dp))
                    favorites.forEach { weather ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSelectFavorite(weather)
                                    favoritesVisible = false
                                }
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("${weather.city}, ${weather.country}")
                                Text("Última temp: ${weather.temperature.current.toInt()}°C")
                            }
                            IconButton(onClick = { onDeleteFavorite(weather) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                            }
                        }
                    }
                }
            }
        }
    }
}