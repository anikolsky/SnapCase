package com.omtorney.snapcase.settings.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.omtorney.snapcase.R
import com.omtorney.snapcase.settings.presentation.CheckPeriod

@Composable
fun MenuDropdown(
    modifier: Modifier = Modifier,
    icon: @Composable (() -> Unit)? = null,
    title: @Composable () -> Unit,
    subtitle: String,
    expanded: Boolean,
    items: List<String>,
    selectedItemText: String,
    onItemSelected: (CheckPeriod) -> Unit,
    onClick: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    ElementFrame(
        modifier = modifier.wrapContentSize(Alignment.TopStart),
        icon = icon,
        title = title,
        subtitle = subtitle
    ) {
        Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier.clickable { onClick() }
            ) {
                Text(
                    text = selectedItemText,
                    style = MaterialTheme.typography.body1
                )
                Icon(
                    painter = painterResource(R.drawable.ic_round_arrow_drop_down),
                    contentDescription = "Drop down"
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = onDismissRequest
            ) {
                items.forEachIndexed { index, element ->
                    DropdownMenuItem(
                        onClick = {
                            onItemSelected(CheckPeriod.values()[index])
                            onDismissRequest()
                        }
                    ) {
                        Text(
                            text = element,
                            style = MaterialTheme.typography.body1
                        )
                    }
                }
            }
        }
    }
}
