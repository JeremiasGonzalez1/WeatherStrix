package com.jg.weatherstrix.presentation.screen
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.location.LocationServices
import com.google.maps.android.compose.rememberCameraPositionState
import com.jg.weatherstrix.presentation.components.WeatherMapScreen
import timber.log.Timber

@Composable
fun MapScreen(viewModel: MapViewModel = hiltViewModel<MapViewModel>()) {

    val cameraPositionState = rememberCameraPositionState()
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val activeLocation by viewModel.activeLocation.collectAsState()
    val weatherState by viewModel.weatherState.collectAsState()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.fetchUserLocation(context, fusedLocationClient)
        } else {
            Timber.e("Location permission was denied by the user.")
            viewModel.setPermissionDenied()
        }
    }

    LaunchedEffect(Unit) {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) -> {
                viewModel.fetchUserLocation(context, fusedLocationClient)
            }
            else -> {
                permissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    WeatherMapScreen(
        weatherState = weatherState,
        activeLocation = activeLocation,
        cameraPositionState = cameraPositionState,
        onMapClick = { latLng -> viewModel.setActiveLocation(latLng) },
        onBackToUserLocation = { viewModel.backToUserLocation() },
        onRequestPermission = { permissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION) }
    )
}

