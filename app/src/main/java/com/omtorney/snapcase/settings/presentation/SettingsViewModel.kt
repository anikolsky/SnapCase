package com.omtorney.snapcase.settings.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.omtorney.snapcase.common.domain.Repository
import com.omtorney.snapcase.common.presentation.logd
import com.omtorney.snapcase.common.util.Constants
import com.omtorney.snapcase.work.CaseCheckWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: Repository,
    private val workManager: WorkManager
) : ViewModel() {

    private val _state = mutableStateOf(SettingsState())
    val state: State<SettingsState> = _state

    val workInfo =
        workManager.getWorkInfosForUniqueWorkLiveData(Constants.WORKER_UNIQUE_PERIODIC_WORK_NAME)
            .map { workInfos ->
                workInfos.firstOrNull()
            }

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.SetAccentColor -> {
                viewModelScope.launch {
                    repository.setAccentColor(event.color.toArgb().toLong())
                }
            }

            is SettingsEvent.OnPermissionResult -> {
                if (!event.isGranted && !state.value.visiblePermissionDialogQueue.contains(event.permission)) {
                    _state.value = state.value.copy(
                        visiblePermissionDialogQueue = state.value.visiblePermissionDialogQueue + event.permission
                    )
                }
            }

            SettingsEvent.CancelWork -> {
                logd("Cancelling work...")
                workManager.cancelAllWork()
            }

            SettingsEvent.SchedulePeriodicWork -> {
                val constraints = setWorkConstraints()

                val request = PeriodicWorkRequestBuilder<CaseCheckWorker>(
                    repeatInterval = Constants.WORK_REPEAT_INTERVAL,
                    repeatIntervalTimeUnit = TimeUnit.MINUTES,
                )
                    .setConstraints(constraints)
                    .build()

                logd("Enqueueing work...")
                workManager.enqueueUniquePeriodicWork(
                    Constants.WORKER_UNIQUE_PERIODIC_WORK_NAME,
                    ExistingPeriodicWorkPolicy.UPDATE,
                    request
                )
//                logd("Work state: ${workInfo.value}")
            }

            SettingsEvent.DismissDialog -> {
                if (state.value.visiblePermissionDialogQueue.isNotEmpty()) {
                    _state.value = _state.value.copy(
                        visiblePermissionDialogQueue = state.value.visiblePermissionDialogQueue.drop(1)
                    )
                }
            }
        }
    }

    private fun setWorkConstraints() =
        Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
}
