package com.jg.weatherstrix.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp

@Composable
fun FavoritesButton(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(48.dp)
            .height(48.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Icon(
            Icons.Default.FavoriteBorder,
            contentDescription = "Favoritos",
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
} 