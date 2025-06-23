package com.jg.weatherstrix.presentation.screen

import com.jg.weatherstrix.domain.models.Weather
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.jg.weatherstrix.domain.models.StatusResult
import com.jg.weatherstrix.domain.usecases.CurrentWeatherUseCase
import com.jg.weatherstrix.domain.usecases.DeleteFavoriteUseCase
import com.jg.weatherstrix.domain.usecases.GetFavoriteUseCase
import com.jg.weatherstrix.domain.usecases.SetFavoriteWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

sealed class MapUIState {
    data class Success(val weather: Weather): MapUIState()
    data class SuccessList(val weathers: List<Weather>): MapUIState()
    data class Error(val messageError: String) : MapUIState()
    data object Loading : MapUIState()
    data object PermissionDenied : MapUIState()
    data object Idle : MapUIState()
}

@HiltViewModel
class MapViewModel @Inject constructor(
    private val weatherUseCase: CurrentWeatherUseCase,
    private val saveFavoriteLocationUseCase: SetFavoriteWeatherUseCase,
    private val listFavoriteLocationUseCase: GetFavoriteUseCase,
    private val deleteFavoriteLocationUseCase: DeleteFavoriteUseCase,
) : ViewModel() {

    private val _weatherState = MutableStateFlow<MapUIState>(MapUIState.Loading)
    val weatherState: StateFlow<MapUIState> = _weatherState.asStateFlow()

    private val _activeLocation = MutableStateFlow<LatLng?>(null)
    val activeLocation: StateFlow<LatLng?> = _activeLocation.asStateFlow()

    private val _favoritesWeather = MutableStateFlow<MapUIState>(MapUIState.Idle)
    val favoritesWeather: StateFlow<MapUIState> = _favoritesWeather.asStateFlow()

    private var userLocation: LatLng? = null

    private fun setUserLocation(latLng: LatLng, context: Context) {
        userLocation = latLng
        setActiveLocation(latLng, context)
    }

    fun getFavoritesLocations() {
        viewModelScope.launch {
            _favoritesWeather.value = MapUIState.Loading
            listFavoriteLocationUseCase.invoke().collect { list ->
                if (list.isNotEmpty()) {
                    _favoritesWeather.value = MapUIState.SuccessList(list)
                } else {
                    _favoritesWeather.value = MapUIState.Idle
                }
            }
        }
    }

    fun deleteFavoriteLocation(weather: Weather) {
        viewModelScope.launch {
            deleteFavoriteLocationUseCase.invoke(weather)
            getFavoritesLocations()
        }
    }

    fun saveLocation() {
        _weatherState.value.let {
            when (it) {
                is MapUIState.Success -> {
                    viewModelScope.launch {
                        saveFavoriteLocationUseCase.invoke(it.weather)
                        getFavoritesLocations()
                    }
                }
                else -> {}
            }
        }
    }

    fun setActiveLocation(latLng: LatLng, context: Context) {
        _activeLocation.value = latLng

        if (!isNetworkAvailable(context)) {
            val lat = "%.4f".format(latLng.latitude)
            val lon = "%.4f".format(latLng.longitude)
            _weatherState.value = MapUIState.Error("Sin conexión a internet.\nUbicación: $lat, $lon")
            return
        }

        _weatherState.value = MapUIState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            when (val response = weatherUseCase.invoke(latLng.latitude, latLng.longitude)) {
                is StatusResult.Error -> _weatherState.value = MapUIState.Error(response.message)
                is StatusResult.Success -> _weatherState.value = MapUIState.Success(response.value)
            }
        }
    }

    fun backToUserLocation(context: Context) {
        userLocation?.let { setActiveLocation(it, context) }
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
                        setUserLocation(userLatLng, context)
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

    @Suppress("DEPRECATION")
    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            return networkInfo.isConnected
        }
    }
}