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
import com.jg.weatherstrix.domain.models.Weather
import com.jg.weatherstrix.presentation.components.WeatherContentMap
import timber.log.Timber

@Composable
fun MapScreen(viewModel: MapViewModel = hiltViewModel<MapViewModel>()) {

    val cameraPositionState = rememberCameraPositionState()
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val activeLocation by viewModel.activeLocation.collectAsState()
    val weatherState by viewModel.weatherState.collectAsState()
    val favoritesState by viewModel.favoritesWeather.collectAsState()
    val isFavoritesVisible by viewModel.isFavoritesVisible.collectAsState()
    val isInfoPanelVisible by viewModel.isInfoPanelVisible.collectAsState()
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.fetchUserLocation(context, fusedLocationClient)
            viewModel.showInfoPanel()
        } else {
            Timber.e("Location permission was denied by the user.")
            viewModel.setPermissionDenied()
        }
    }
    val favorites: List<Weather> = when (favoritesState) {
        is MapUIState.SuccessList -> (favoritesState as MapUIState.SuccessList).weathers
        else -> emptyList()
    }


    LaunchedEffect(Unit) {
        viewModel.getFavoritesLocations()
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) -> {
                viewModel.fetchUserLocation(context, fusedLocationClient)
                viewModel.showInfoPanel()
            }
            else -> {
                permissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }


    WeatherContentMap(
        weatherState = weatherState,
        activeLocation = activeLocation,
        cameraPositionState = cameraPositionState,
        onMapClick = { latLng -> viewModel.setActiveLocation(latLng, context) },
        onBackToUserLocation = { viewModel.backToUserLocation(context) },
        onRequestPermission = { permissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION) },
        saveLocation = { viewModel.saveLocation() },
        favorites = favorites,
        isFavoritesVisible = isFavoritesVisible,
        onShowFavorites = { viewModel.showFavorites() },
        onHideFavorites = { viewModel.hideFavorites() },
        isInfoPanelVisible = isInfoPanelVisible,
        onShowInfoPanel = { viewModel.showInfoPanel() },
        onHideInfoPanel = { viewModel.hideInfoPanel() },
        onSelectFavorite = { weather -> viewModel.selectFavorite(weather, context) },
        onDeleteFavorite = { weather -> viewModel.deleteFavoriteLocation(weather) }
    )
}

