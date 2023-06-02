package com.omtorney.snapcase.settings.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.omtorney.snapcase.R
import com.omtorney.snapcase.common.presentation.theme.SnapCaseTheme

@Preview(
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun SettingsPreview() {
    SnapCaseTheme {
        Surface {
            Column {
                MenuButton(
                    accentColor = Color.Blue,
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_round_color),
                            contentDescription = "Worker"
                        )
                    },
                    title = { Text(text = "Button") },
                    subtitle = "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium",
                    buttonText = "Choose",
                    onClick = {}
                )
                MenuSwitcher(
                    accentColor = Color.Blue,
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_round_star),
                            contentDescription = "Worker"
                        )
                    },
                    title = { Text(text = "Switcher") },
                    subtitle = "Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis",
                    state = true,
                    onCheckedChange = {}
                )
                MenuDropdown(
                    accentColor = Color.Blue,
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_round_timer),
                            contentDescription = "Time period"
                        )
                    },
                    title = { Text(text = "Dropdown") },
                    subtitle = "Selected period: 15 minutes",
                    expanded = false,
                    onClickButton = {},
                    onClickMenu = {},
                    onDismissRequest = {}
                )
            }
        }
    }
}
