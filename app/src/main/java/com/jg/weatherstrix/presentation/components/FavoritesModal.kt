package com.jg.weatherstrix.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jg.weatherstrix.domain.models.Weather

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesModal(
    visible: Boolean,
    favorites: List<Weather>,
    onDismiss: () -> Unit,
    onSelectFavorite: (Weather) -> Unit,
    onDeleteFavorite: (Weather) -> Unit
) {
    if (visible) {
        ModalBottomSheet(onDismissRequest = onDismiss) {
            Column {
                Text("Favoritos", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(16.dp))
                favorites.forEach { weather ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clickable { onSelectFavorite(weather) }
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("${weather.city}, ${weather.country}")
                            Text("Última temp: ${weather.temperature.current.toInt()}°C")
                        }
                        IconButton(onClick = { onDeleteFavorite(weather) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                        }
                    }
                }
            }
        }
    }
} 