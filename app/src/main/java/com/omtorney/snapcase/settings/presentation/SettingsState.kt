package com.omtorney.snapcase.settings.presentation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import androidx.work.WorkInfo

data class SettingsState(
    val workInfo: LiveData<WorkInfo?>? = null,
    val visiblePermissionDialogQueue: SnapshotStateList<String> = mutableStateListOf()
)
