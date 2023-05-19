package com.omtorney.snapcase.settings.presentation

import androidx.compose.ui.graphics.Color

sealed class SettingsEvent {
    data class SetAccentColor(val color: Color) : SettingsEvent()
    data class OnPermissionResult(val permission: String, val isGranted: Boolean) : SettingsEvent()
    object SchedulePeriodicWork : SettingsEvent()
    object CancelWork : SettingsEvent()
    object DismissDialog : SettingsEvent()
}
