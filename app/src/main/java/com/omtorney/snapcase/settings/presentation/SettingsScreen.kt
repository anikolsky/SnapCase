package com.omtorney.snapcase.settings.presentation

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.work.WorkInfo
import com.omtorney.snapcase.BuildConfig
import com.omtorney.snapcase.R
import com.omtorney.snapcase.common.presentation.Screen
import com.omtorney.snapcase.common.presentation.components.BackButton
import com.omtorney.snapcase.common.presentation.components.PermissionDialog
import com.omtorney.snapcase.common.presentation.components.PostNotificationsPermissionTextProvider
import com.omtorney.snapcase.common.presentation.components.TopBar
import com.omtorney.snapcase.common.presentation.components.TopBarTitle
import com.omtorney.snapcase.settings.presentation.components.MenuDropdown
import com.omtorney.snapcase.settings.presentation.components.MenuSwitcher

@Composable
fun SettingsScreen(
    state: SettingsState,
    workInfo: WorkInfo?,
    onEvent: (SettingsEvent) -> Unit,
    accentColor: Color,
    onBackClick: () -> Unit,
    onAppSettingsClick: () -> Unit
) {
    val context = LocalContext.current
    val activity = context as Activity
//    val workInfo = state.workInfo?.observeAsState()?.value
    var darkThemeState by remember { mutableStateOf(state.isDarkThemeEnabled) }
    var workState by remember { mutableStateOf(workInfo?.state == WorkInfo.State.ENQUEUED) }
    val dialogQueue = state.visiblePermissionDialogQueue
    var backgroundCheckMenuExpanded by remember { mutableStateOf(false) }
    var backgroundCheckPeriod by remember { mutableStateOf(state.backgroundCheckPeriod) }
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
        MenuSwitcher(
            accentColor = accentColor,
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_round_star),
                    contentDescription = "Worker"
                )
            },
            title = { Text(text = "Отслеживание дел") },
            subtitle = "Включите для получения уведомлений при обновлении на сайте информации об избранных делах" +
                    "\n\nСостояние: ${workInfo?.state ?: "EMPTY"}",
            state = workState,
            onCheckedChange = { checked ->
                workState = checked
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                    == PackageManager.PERMISSION_GRANTED
                ) {
                    if (workState) {
                        onEvent(SettingsEvent.SchedulePeriodicWork)
                    } else {
                        onEvent(SettingsEvent.CancelWork)
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        storagePermissionResultLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }
            }
        )
        MenuDropdown(
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_round_timer),
                    contentDescription = "Time period"
                )
            },
            title = { Text(text = "Период проверки дел") },
            subtitle = "Функция отслеживания будет перезапущена при изменении периода",
            expanded = backgroundCheckMenuExpanded,
            items = CheckPeriod.values().map { it.title },
            selectedItemText = backgroundCheckPeriod.title,
            onItemSelected = { periodTitle ->
                onEvent(SettingsEvent.SetBackgroundCheckPeriod(periodTitle))
                backgroundCheckPeriod = periodTitle
            },
            onClick = { backgroundCheckMenuExpanded = true },
            onDismissRequest = { backgroundCheckMenuExpanded = false }
        )
        MenuSwitcher(
            accentColor = accentColor,
            icon = {
               Icon(
                   painter = painterResource(R.drawable.ic_round_invert_colors),
                   contentDescription = "Dark mode"
               )
            },
            title = {
                Text(text = "Темный режим")
            },
            subtitle = "Включить темный режим приложения",
            state = darkThemeState,
            onCheckedChange = {  checked ->
                onEvent(SettingsEvent.EnableDarkTheme(checked))
                darkThemeState = checked
            }
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        Text(
            text = "Версия сборки: ${BuildConfig.VERSION_CODE}",
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        )
    }

    LaunchedEffect(workInfo) {
        workState = workInfo?.state == WorkInfo.State.ENQUEUED && !state.isWorkInProgress
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
                onAppSettingsClick = onAppSettingsClick
            )
        }
}
