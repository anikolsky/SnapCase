package com.omtorney.snapcase.settings.presentation

import androidx.work.WorkInfo

data class SettingsState(
//    val workInfo: WorkInfo? = null,
    val visiblePermissionDialogQueue: List<String> = emptyList()
)
