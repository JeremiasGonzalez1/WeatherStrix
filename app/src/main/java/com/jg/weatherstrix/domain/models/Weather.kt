package com.jg.weatherstrix.domain.models
import kotlinx.serialization.Serializable

@Serializable
data class Weather(
    val id :Int,
    val conditions: List<Conditions>,
    val country:String,
    val city:String,
    val coord:Coord,
    val temperature: Temperature
)

@Serializable
data class Conditions(
    val status:String,
    val description:String,
)

@Serializable
data class Coord(
    val lat: Double,
    val lon: Double
)

@Serializable
data class Temperature(
    val current: Double,
    val max: Double,
    val min: Double,
    val feelsLike: Double
)