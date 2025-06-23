package com.jg.weatherstrix.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.jg.weatherstrix.domain.models.Weather
import com.jg.weatherstrix.presentation.screen.MapUIState
@Composable
fun WeatherContentMap(
    weatherState: MapUIState,
    activeLocation: LatLng?,
    cameraPositionState: CameraPositionState,
    onMapClick: (LatLng) -> Unit,
    onBackToUserLocation: () -> Unit,
    onRequestPermission: () -> Unit,
    saveLocation: () -> Unit = {},
    favorites: List<Weather> = emptyList(),
    isFavoritesVisible: Boolean = false,
    onShowFavorites: () -> Unit = {},
    onHideFavorites: () -> Unit = {},
    isInfoPanelVisible: Boolean = false,
    onShowInfoPanel: () -> Unit = {},
    onHideInfoPanel: () -> Unit = {},
    onSelectFavorite: (Weather) -> Unit = {},
    onDeleteFavorite: (Weather) -> Unit = {}
) {
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
            if (isInfoPanelVisible) {
                WeatherPanelContainer(
                    weatherState = weatherState,
                    onRequestPermission = onRequestPermission,
                    saveLocation = saveLocation,
                    onBackToUserLocation = onBackToUserLocation,
                    onHidePanel = onHideInfoPanel
                )
            } else {
                PanelToggleButton(isVisible = false, onToggle = onShowInfoPanel)
            }

            Spacer(Modifier.height(8.dp))
            FavoritesButton(onClick = onShowFavorites)
        }

        FavoritesModal(
            visible = isFavoritesVisible,
            favorites = favorites,
            onDismiss = onHideFavorites,
            onSelectFavorite = onSelectFavorite,
            onDeleteFavorite = onDeleteFavorite
        )
    }
}