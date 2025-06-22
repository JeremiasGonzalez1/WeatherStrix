data class Weather(
    val conditions: List<Conditions>,
    val country:String,
    val city:String,
    val coord:Coord
)

data class Conditions(
    val status:String,
    val description:String,
)

data class  Coord(
    val lat: Double,
    val lon: Double
)