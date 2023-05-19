package com.omtorney.snapcase.settings.presentation

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.work.WorkInfo
import com.github.skydoves.colorpicker.compose.AlphaSlider
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.omtorney.snapcase.BuildConfig
import com.omtorney.snapcase.R
import com.omtorney.snapcase.common.presentation.Screen
import com.omtorney.snapcase.common.presentation.components.BackButton
import com.omtorney.snapcase.common.presentation.components.PermissionDialog
import com.omtorney.snapcase.common.presentation.components.PostNotificationsPermissionTextProvider
import com.omtorney.snapcase.common.presentation.components.TopBar
import com.omtorney.snapcase.common.presentation.components.TopBarTitle
import com.omtorney.snapcase.common.presentation.logd
import com.omtorney.snapcase.common.presentation.theme.SnapCaseTheme

@Composable
fun SettingsScreen(
    state: SettingsState,
    onEvent: (SettingsEvent) -> Unit,
    accentColor: Color,
    onBackClick: () -> Unit,
    onGoToAppSettingsClick: () -> Unit
) {
    val context = LocalContext.current
    val activity = context as Activity
    val workInfo = state.workInfo?.observeAsState()?.value
    var workState by remember { mutableStateOf(workInfo?.state == WorkInfo.State.ENQUEUED) }
    var colorPickerOpened by remember { mutableStateOf(false) }
    val dialogQueue = state.visiblePermissionDialogQueue
    val storagePermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                onEvent(
                    SettingsEvent.OnPermissionResult(
                        permission = Manifest.permission.POST_NOTIFICATIONS,
                        isGranted = isGranted
                    )
                )
            }
        }
    )

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxSize()
    ) {
        TopBar {
            BackButton(
                onBackClick = onBackClick
            )
            TopBarTitle(
                title = Screen.Settings.title,
                modifier = Modifier.weight(1f)
            )
        }
//        MenuButton(
//            accentColor = accentColor,
//            icon = {
//                Icon(
//                    painter = painterResource(R.drawable.round_color),
//                    contentDescription = "Color"
//                )
//            },
//            title = { Text(text = "Акцентный цвет") },
//            subtitle = "Выберите дополнительный цвет приложения",
//            buttonText = "Выбрать",
//            onClick = { colorPickerOpened = true }
//        )
        MenuSwitcher(
            accentColor = accentColor,
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_round_star),
                    contentDescription = "Worker"
                )
            },
            title = { Text(text = "Отслеживание дел") },
            subtitle = "Включите для получения уведомлений при появлении новых записей о ходе рассмотрения избранных дел",
            state = workState,
            onCheckedChange = { checked ->
                logd("switcher on: $checked")
                workState = checked
                logd("workState: $workState")
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                    == PackageManager.PERMISSION_GRANTED
                ) {
                    if (workState) {
                        logd("Scheduling work...")
                        onEvent(SettingsEvent.SchedulePeriodicWork)
                    } else {
                        logd("Cancelling work...")
                        onEvent(SettingsEvent.CancelWork)
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        storagePermissionResultLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }
            }
        )
        Text(
            text = "Состояние: ${workInfo?.state ?: "EMPTY"}",
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.onBackground.copy(alpha = 0.5f),
            modifier = Modifier.padding(start = 53.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        Text(
            text = "Версия сборки: ${BuildConfig.VERSION_CODE}",
            color = MaterialTheme.colors.onBackground.copy(alpha = 0.5f),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        )
    }

    /** Color picker dialog */
    if (colorPickerOpened) {
        ColorPickerDialog(
            accentColor = accentColor,
            onSelectClick = { onEvent(SettingsEvent.SetAccentColor(it)) },
            onDismiss = { colorPickerOpened = false }
        )
    }

    /** Permission dialog */
    dialogQueue
        .reversed()
        .forEach { permission ->
            PermissionDialog(
                permissionTextProvider = when (permission) {
                    Manifest.permission.POST_NOTIFICATIONS -> {
                        PostNotificationsPermissionTextProvider()
                    }
                    else -> return@forEach
                },
                isPermanentlyDeclined = !activity.shouldShowRequestPermissionRationale(permission),
                onDismiss = { onEvent(SettingsEvent.DismissDialog) },
                onOkClick = { onEvent(SettingsEvent.DismissDialog) },
                onGoToAppSettingsClick = onGoToAppSettingsClick
            )
        }
}

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
            colors = ButtonDefaults.buttonColors(
                backgroundColor = accentColor
            ),
        ) {
            Text(text = buttonText)
        }
    }
}

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

@Composable
fun ElementFrame(
    modifier: Modifier = Modifier,
    icon: @Composable (() -> Unit)? = null,
    title: @Composable () -> Unit,
    subtitle: String,
    content: @Composable () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(14.dp)
    ) {
        icon?.invoke()
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 14.dp)
        ) {
            title()
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.onBackground.copy(alpha = 0.5f)
            )
        }
        content()
    }
}

@Composable
fun ColorPickerDialog(
    accentColor: Color,
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
                    logd("Color ${it.hexCode}")
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
                    colors = ButtonDefaults.buttonColors(accentColor)
                ) {
                    Text(text = stringResource(R.string.dismiss))
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        onSelectClick(controller.selectedColor.value)
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(accentColor)
                ) {
                    Text(text = stringResource(R.string.select))
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    uiMode = UI_MODE_NIGHT_YES
)
@Composable
fun SettingsPreview() {
    SnapCaseTheme {
        Surface {
            Column {
                MenuButton(
                    accentColor = Color.Magenta,
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.round_color),
                            contentDescription = "Worker"
                        )
                    },
                    title = { Text(text = "Button") },
                    subtitle = "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium",
                    buttonText = "Choose",
                    onClick = {}
                )
                MenuSwitcher(
                    accentColor = Color.Magenta,
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
            }
        }
    }
}
