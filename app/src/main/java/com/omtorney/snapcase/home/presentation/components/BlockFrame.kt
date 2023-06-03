package com.omtorney.snapcase.home.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BlockFrame(
    modifier: Modifier = Modifier,
    accentColor: Color,
    content: @Composable () -> Unit
) {
    Card(
        shape = MaterialTheme.shapes.extraSmall,
        colors = CardDefaults.cardColors(accentColor.copy(alpha = 0.2f)),
        modifier = modifier.padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            content()
        }
    }
}
