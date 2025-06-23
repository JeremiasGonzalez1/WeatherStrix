package com.jg.weatherstrix.presentation.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import timber.log.Timber

@Composable
fun GoogleMapView(cameraPositionState: CameraPositionState, userLocation: LatLng?, updateLocation:(LatLng)->Unit) {
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        onMapClick = {latlng ->
            Timber.d("Map tapped at: $latlng")
            updateLocation(latlng)
        }
    ) {
        userLocation?.let {
            Marker(
                state = MarkerState(position = it),
                title = "Tu ubicación",
                snippet = "Aquí es donde te encuentras actualmente."
            )
            cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 13f)
        }
    }
}