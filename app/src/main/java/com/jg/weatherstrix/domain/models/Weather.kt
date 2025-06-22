data class Weather(
    val conditions: List<Conditions>,
    val country:String,
    val city:String,
    val coord:Coord,
    val temperature: Temperature
)

data class Conditions(
    val status:String,
    val description:String,
)

data class  Coord(
    val lat: Double,
    val lon: Double
)

data class Temperature(
    val current: Double,
    val max: Double,
    val min: Double,
    val feelsLike: Double
)