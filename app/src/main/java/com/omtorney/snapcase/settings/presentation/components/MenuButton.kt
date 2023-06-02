package com.omtorney.snapcase.settings.presentation.components

import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
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
            colors = ButtonDefaults.buttonColors(backgroundColor = accentColor),
        ) {
            Text(text = buttonText)
        }
    }
}
