package com.jg.weatherstrix.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.jg.weatherstrix.domain.models.Weather
import com.jg.weatherstrix.presentation.screen.MapUIState
import androidx.compose.ui.graphics.vector.ImageVector

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
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 16.dp)
                .fillMaxHeight(0.75f), // 3/4 de la pantalla
            verticalArrangement = Arrangement.Bottom
        ) {
            if (!isInfoPanelVisible) {
                ActionFabButton(
                    icon =  Icons.Default.KeyboardArrowRight ,
                    contentDescription =  "Mostrar panel",
                    onClick = onShowInfoPanel
                )
            }
            Spacer(Modifier.height(16.dp))

            ActionFabButton(
                icon = Icons.Default.LocationOn,
                contentDescription = "Mi ubicación",
                onClick = onBackToUserLocation
            )
            Spacer(Modifier.height(16.dp))
            ActionFabButton(
                icon = Icons.Default.FavoriteBorder,
                contentDescription = "Favoritos",
                onClick = onShowFavorites
            )

        }
        if (isInfoPanelVisible) {
            Column(
                Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            ) {
                WeatherPanelContainer(
                    weatherState = weatherState,
                    onRequestPermission = onRequestPermission,
                    saveLocation = saveLocation,
                    onHidePanel = onHideInfoPanel,
                )
            }
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

@Composable
fun ActionFabButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        elevation = FloatingActionButtonDefaults.elevation(8.dp),
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier.size(56.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription
        )
    }
}