package com.omtorney.snapcase.presentation.screen.settings

data class SettingsState(
//    val workInfo: WorkInfo? = null,
    val backgroundCheckPeriod: CheckPeriod = CheckPeriod.ONE_HOUR,
    val isWorkInProgress: Boolean = false,
    val isDarkThemeEnabled: Boolean = false,
    val visiblePermissionDialogQueue: List<String> = emptyList()
)
