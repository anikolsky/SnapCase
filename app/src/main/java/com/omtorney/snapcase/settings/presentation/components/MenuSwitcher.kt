package com.omtorney.snapcase.settings.presentation.components

import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun MenuSwitcher(
    modifier: Modifier = Modifier,
    accentColor: Color,
    icon: @Composable (() -> Unit)? = null,
    title: @Composable () -> Unit,
    subtitle: String,
    state: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    ElementFrame(
        modifier = modifier,
        icon = icon,
        title = title,
        subtitle = subtitle
    ) {
        Switch(
            checked = state,
            onCheckedChange = { onCheckedChange(it) },
            colors = SwitchDefaults.colors(
                checkedThumbColor = accentColor,
                uncheckedThumbColor = Color.Gray
            )
        )
    }
}
