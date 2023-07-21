package com.omtorney.snapcase.presentation.screen.settings

import android.content.Context
import com.omtorney.snapcase.data.remote.database.FirestoreUser

sealed class SettingsEvent {
//    data class SetAccentColor(val color: Color) : SettingsEvent()
    data class SetBackgroundCheckPeriod(val period: CheckPeriod) : SettingsEvent()
    data class OnPermissionResult(val permission: String, val isGranted: Boolean) : SettingsEvent()
    data class EnableDarkTheme(val enabled: Boolean) : SettingsEvent()
    object SchedulePeriodicWork : SettingsEvent()
    object CancelWork : SettingsEvent()
    object DismissDialog : SettingsEvent()
    data class CreateBackup(val firestoreUserName: String, val context: Context) : SettingsEvent()
    data class UpdateBackup(val firestoreUser: FirestoreUser, val context: Context) : SettingsEvent()
    data class DeleteBackup(val firestoreUser: FirestoreUser, val context: Context) : SettingsEvent()
}
