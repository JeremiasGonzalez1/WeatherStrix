package com.jg.weatherstrix.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherDetailDTO(
    val base: String? = null,
    val clouds: Clouds? = null,
    val cod: Int? = null,
    val coord: Coord,
    val dt: Int? = null,
    val id: Int? = null,
    val main: Main,
    val name: String,
    val sys: Sys? = null,
    val timezone: Int? = null,
    val visibility: Int? = null,
    val weather: List<WeatherDTO>,
    val wind: Wind? = null
)

@Serializable
data class Clouds(
    val all: Int
)

@Serializable
data class Coord(
    val lat: Double,
    val lon: Double
)

@Serializable
data class Main(
    @SerialName("feels_like")
    val feelsLike: Double? = null,
    @SerialName("grnd_level")
    val grndLevel: Int? = null,
    val humidity: Int? = null,
    val pressure: Int? = null,
    @SerialName("sea_level")
    val seaLevel: Int? = null,
    val temp: Double,
    @SerialName("temp_max")
    val tempMax: Double,
    @SerialName("temp_min")
    val tempMin: Double
)

@Serializable
data class Sys(
    val country: String? = null,
    val id: Int? = null,
    val sunrise: Int? = null,
    val sunset: Int? = null,
    val type: Int? = null
)

@Serializable
data class WeatherDTO(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)

@Serializable
data class Wind(
    val deg: Int,
    val gust: Double? = null,
    val speed: Double
)