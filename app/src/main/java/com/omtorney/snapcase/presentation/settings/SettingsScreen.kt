package com.omtorney.snapcase.presentation.settings

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.skydoves.colorpicker.compose.*
import com.omtorney.snapcase.R
import com.omtorney.snapcase.presentation.common.AppName
import com.omtorney.snapcase.presentation.common.BackButton
import com.omtorney.snapcase.presentation.common.TopBar
import com.omtorney.snapcase.presentation.ui.theme.SnapCaseTheme

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val accentColor by viewModel.accentColor.collectAsState()
    var colorPickerOpened by remember { mutableStateOf(false) }

    Column {
        TopBar {
            BackButton { onBackClick() }
            AppName(modifier = Modifier.weight(1f))
        }
        SettingsMenuButton(
            icon = R.drawable.round_color,
            title = "Accent color",
            subtitle = "Select general color for application",
            onClick = { colorPickerOpened = true }
        )
    }

    if (colorPickerOpened) {
        ColorPickerDialog(
            accentColor = accentColor,
            onSelectClick = { viewModel.setAccentColor(it) },
            onDismiss = { colorPickerOpened = false }
        )
    }
}

@Composable
fun SettingsMenuButton(
    @DrawableRes icon: Int,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = title
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        ) {
            Text(text = title)
            Text(text = subtitle, style = MaterialTheme.typography.caption)
        }
        Button(onClick = onClick) {
            Text(text = "Select")
        }
    }
}

@Composable
fun ColorPickerDialog(
    accentColor: Long,
    onSelectClick: (Color) -> Unit,
    onDismiss: () -> Unit
) {
    val controller = rememberColorPickerController()

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(color = MaterialTheme.colors.background)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AlphaTile(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .clip(RoundedCornerShape(6.dp)),
                    controller = controller
                )
            }
            HsvColorPicker(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp)
                    .padding(10.dp),
                controller = controller,
                onColorChanged = {
                    Log.d("Color", it.hexCode)
                }
            )
            AlphaSlider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .height(35.dp),
                controller = controller,
                tileOddColor = Color.White,
                tileEvenColor = Color.Black
            )
            BrightnessSlider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .height(35.dp),
                controller = controller,
            )
            Row {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(Color(accentColor))
                ) {
                    Text(text = stringResource(R.string.dismiss))
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        onSelectClick(controller.selectedColor.value)
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(Color(accentColor))
                ) {
                    Text(text = stringResource(R.string.select))
                }
            }
        }
    }
}

@Preview
@Composable
fun SettingsMenuButtonPreview() {
    SnapCaseTheme {
        SettingsMenuButton(
            icon = R.drawable.round_color,
            title = "Title",
            subtitle = "subtitle",
        {})
    }
}