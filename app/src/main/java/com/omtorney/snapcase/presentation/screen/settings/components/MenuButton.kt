package com.omtorney.snapcase.presentation.screen.settings.components

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun MenuButton(
    modifier: Modifier = Modifier,
    accentColor: Color,
    icon: @Composable (() -> Unit)? = null,
    title: @Composable () -> Unit,
    subtitle: String,
    buttonText: String,
    onClick: () -> Unit
) {
    ElementFrame(
        modifier = modifier,
        icon = icon,
        title = title,
        subtitle = subtitle
    ) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(containerColor = accentColor),
            shape = MaterialTheme.shapes.extraSmall
        ) {
            Text(text = buttonText)
        }
    }
}
