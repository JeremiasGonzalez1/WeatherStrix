import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jg.weatherstrix.presentation.screen.MapScreen
import kotlinx.serialization.Serializable

@Composable
fun WeatherNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(modifier = modifier, navController =navController , startDestination = Map){
        composable<Map>{
            MapScreen()
        }
    }
}


@Serializable
object Map
