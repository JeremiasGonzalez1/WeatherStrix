package com.jg.weatherstrix.presentation.screen

import Weather
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.jg.weatherstrix.domain.models.StatusResult
import com.jg.weatherstrix.domain.usecases.CurrentWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

sealed class MapUIState{
    data class Success(val weather: Weather): MapUIState()
    data class Error (val messageError: String) : MapUIState()
    data object Loading : MapUIState()
    data object PermissionDenied : MapUIState()
}

@HiltViewModel
class MapViewModel @Inject constructor(private val useCase: CurrentWeatherUseCase) : ViewModel() {

    private val _weatherState = MutableStateFlow<MapUIState>(MapUIState.Loading)
    val weatherState: StateFlow<MapUIState> = _weatherState.asStateFlow()

    private val _activeLocation = MutableStateFlow<LatLng?>(null)
    val activeLocation: StateFlow<LatLng?> = _activeLocation.asStateFlow()

    private var userLocation: LatLng? = null

    private fun setUserLocation(latLng: LatLng) {
        userLocation = latLng
        setActiveLocation(latLng)
    }

    fun setActiveLocation(latLng: LatLng) {
        _activeLocation.value = latLng
        _weatherState.value = MapUIState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            when (val response = useCase.invoke(latLng.latitude, latLng.longitude)) {
                is StatusResult.Error -> _weatherState.value = MapUIState.Error(response.message)
                is StatusResult.Success -> _weatherState.value = MapUIState.Success(response.value)
            }
        }
    }

    fun backToUserLocation() {
        userLocation?.let { setActiveLocation(it) }
    }

    fun setPermissionDenied() {
        _weatherState.value = MapUIState.PermissionDenied
    }

    fun fetchUserLocation(context: Context, fusedLocationClient: FusedLocationProviderClient) {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    location?.let {
                        val userLatLng = LatLng(it.latitude, it.longitude)
                        setUserLocation(userLatLng)
                    } ?: run {
                        _weatherState.value = MapUIState.Error("No se pudo obtener tu ubicación")
                    }
                }.addOnFailureListener { exception ->
                    _weatherState.value = MapUIState.Error("Error al obtener ubicación: ${exception.message}")
                }
            } catch (e: SecurityException) {
                Timber.e("Permission for location access was revoked: ${e.localizedMessage}")
                _weatherState.value = MapUIState.PermissionDenied
            }
        } else {
            Timber.e("Location permission is not granted.")
            _weatherState.value = MapUIState.PermissionDenied
        }
    }
}