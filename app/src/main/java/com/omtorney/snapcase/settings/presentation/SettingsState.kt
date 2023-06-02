package com.omtorney.snapcase.settings.presentation

import androidx.work.WorkInfo

data class SettingsState(
//    val workInfo: WorkInfo? = null,
    val backgroundCheckPeriod: CheckPeriod = CheckPeriod.ONE_HOUR,
    val isWorkInProgress: Boolean = false,
    val visiblePermissionDialogQueue: List<String> = emptyList()
)
