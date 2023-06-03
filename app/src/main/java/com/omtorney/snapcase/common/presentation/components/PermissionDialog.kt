package com.omtorney.snapcase.common.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PermissionDialog(
    permissionTextProvider: PermissionTextProvider,
    isPermanentlyDeclined: Boolean,
    onDismiss: () -> Unit,
    onOkClick: () -> Unit,
    onGoToAppSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Требуется разрешение")
        },
        text = {
            Text(
                text = permissionTextProvider.getDescription(
                    isPermanentlyDeclined = isPermanentlyDeclined
                )
            )
        },
        confirmButton = {
            Button(onClick = {
                if (isPermanentlyDeclined) {
                    onGoToAppSettingsClick()
                } else {
                    onOkClick()
                }
            }) {
                Text(
                    text = if (isPermanentlyDeclined) {
                        "Предоставить разрешение"
                    } else {
                        "OK"
                    }
                )
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text(text = "Отмена")
            }
        },
        modifier = modifier
    )
}

interface PermissionTextProvider {
    fun getDescription(isPermanentlyDeclined: Boolean): String
}

class PostNotificationsPermissionTextProvider : PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if (isPermanentlyDeclined) {
            "Похоже, Вы отказались от предоставления разрешение на уведомления. " +
                    "Вы можете предоставить его вручную в системных настройках приложения"
        } else {
            "Приложению требуется разрешение на уведомления, чтобы сообщать Вам о наличии обновлений по отслеживаемым делам"
        }
    }
}