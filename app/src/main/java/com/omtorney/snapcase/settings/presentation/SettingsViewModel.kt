package com.omtorney.snapcase.settings.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.omtorney.snapcase.common.util.Constants
import com.omtorney.snapcase.settings.data.SettingsStore
import com.omtorney.snapcase.work.CaseCheckWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settings: SettingsStore,
    private val workManager: WorkManager
) : ViewModel() {

    private val _state = mutableStateOf(SettingsState())
    val state: State<SettingsState> = _state

    val workInfo =
        workManager.getWorkInfosForUniqueWorkLiveData(Constants.WORKER_UNIQUE_PERIODIC_WORK_NAME)
            .map { workInfos ->
                workInfos.firstOrNull()
            }

    private val backgroundCheckPeriodState: StateFlow<CheckPeriod> = settings.getCaseCheckPeriod.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = CheckPeriod.ONE_HOUR
    )

    init {
        viewModelScope.launch {
            val savedBackgroundCheckPeriod = settings.getCaseCheckPeriod.first()
            _state.value = state.value.copy(backgroundCheckPeriod = savedBackgroundCheckPeriod)
        }
    }

    fun onEvent(event: SettingsEvent) {
        when (event) {
//            is SettingsEvent.SetAccentColor -> {
//                viewModelScope.launch {
//                    settings.setAccentColor(event.color.toArgb().toLong())
//                }
//            }

            is SettingsEvent.SetBackgroundCheckPeriod -> {
                viewModelScope.launch {
                    settings.setCaseCheckPeriod(event.period)
                    _state.value = state.value.copy(backgroundCheckPeriod = event.period)
                    schedulePeriodicWork(event.period.period)
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
                _state.value = state.value.copy(isWorkInProgress = true)
                workManager.cancelUniqueWork(Constants.WORKER_UNIQUE_PERIODIC_WORK_NAME)
            }

            SettingsEvent.SchedulePeriodicWork -> {
                _state.value = state.value.copy(isWorkInProgress = true)
                schedulePeriodicWork(backgroundCheckPeriodState.value.period)
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

    private fun setWorkConstraints() = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    private fun schedulePeriodicWork(period: Long) {
        val constraints = setWorkConstraints()

        val request = PeriodicWorkRequestBuilder<CaseCheckWorker>(
            repeatInterval = period,
            repeatIntervalTimeUnit = TimeUnit.MINUTES,
        )
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniquePeriodicWork(
            Constants.WORKER_UNIQUE_PERIODIC_WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )

        _state.value = state.value.copy(isWorkInProgress = false)
    }
}
