package com.omtorney.snapcase.settings.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.omtorney.snapcase.settings.presentation.CheckPeriod

@Composable
fun MenuDropdown(
    modifier: Modifier = Modifier,
    accentColor: Color,
    icon: @Composable (() -> Unit)? = null,
    title: @Composable () -> Unit,
    subtitle: String,
    expanded: Boolean,
    onClickButton: () -> Unit,
    onClickMenu: (CheckPeriod) -> Unit,
    onDismissRequest: () -> Unit
) {
    ElementFrame(
        modifier = modifier,
        icon = icon,
        title = title,
        subtitle = subtitle
    ) {
        Box {
            Button(
                colors = ButtonDefaults.buttonColors(backgroundColor = accentColor),
                onClick = onClickButton
            ) {
                Text(text = "Выбрать")
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest
        ) {
            DropdownMenuItem(
                onClick = {
                    onClickMenu(CheckPeriod.FIFTEEN_MINUTES)
                    onDismissRequest()
                },
                modifier = Modifier.padding(10.dp)
            ) {
                Text(text = CheckPeriod.FIFTEEN_MINUTES.title)
            }
            DropdownMenuItem(
                onClick = {
                    onClickMenu(CheckPeriod.ONE_HOUR)
                    onDismissRequest()
                },
                modifier = Modifier.padding(10.dp)
            ) {
                Text(text = CheckPeriod.ONE_HOUR.title)
            }
            DropdownMenuItem(
                onClick = {
                    onClickMenu(CheckPeriod.SIX_HOURS)
                    onDismissRequest()
                },
                modifier = Modifier.padding(10.dp)
            ) {
                Text(text = CheckPeriod.SIX_HOURS.title)
            }
        }
    }
}
