package com.omtorney.snapcase.presentation.screen.settings

import android.content.Context
import android.widget.Toast
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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.omtorney.snapcase.data.remote.database.FirestoreResult
import com.omtorney.snapcase.data.remote.database.FirestoreUser
import com.omtorney.snapcase.domain.usecase.UseCases
import com.omtorney.snapcase.util.Constants
import com.omtorney.snapcase.firebase.presentation.FirestoreUserState
import com.omtorney.snapcase.work.CaseCheckWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val useCases: UseCases,
    private val workManager: WorkManager
) : ViewModel() {

    private val auth = Firebase.auth

    private val _settingsState = mutableStateOf(SettingsState())
    val settingsState: State<SettingsState> = _settingsState

    val workInfo =
        workManager.getWorkInfosForUniqueWorkLiveData(Constants.WORKER_UNIQUE_PERIODIC_WORK_NAME)
            .map { workInfos ->
                workInfos.firstOrNull()
            }

    private val backgroundCheckPeriodState: StateFlow<CheckPeriod> = useCases.getCaseCheckPeriod().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = CheckPeriod.ONE_HOUR
    )

    val firestoreUserState: StateFlow<FirestoreUserState> = auth.currentUser?.let { user ->
        useCases.getUser(user.displayName!!).map { result ->
            when (result) {
                is FirestoreResult.Success -> FirestoreUserState(data = result.data)
                is FirestoreResult.Error -> FirestoreUserState(errorMessage = result.exception.message)
                is FirestoreResult.Loading -> FirestoreUserState(isLoading = true)
            }
        }.stateIn(
            scope = viewModelScope,
            initialValue = FirestoreUserState(isLoading = true),
            started = SharingStarted.WhileSubscribed(5000)
        )
    } ?: flow { emit(FirestoreUserState()) }.stateIn(
        scope = viewModelScope,
        initialValue = FirestoreUserState(),
        started = SharingStarted.WhileSubscribed(5000)
    )

    init {
        viewModelScope.launch {
            val savedBackgroundCheckPeriod = useCases.getCaseCheckPeriod().first()
            val isDarkThemeEnabled = useCases.isDarkThemeEnabled().first()
            _settingsState.value = settingsState.value.copy(
                backgroundCheckPeriod = savedBackgroundCheckPeriod,
                isDarkThemeEnabled = isDarkThemeEnabled
            )
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
                    useCases.setCaseCheckPeriod(event.period)
                    _settingsState.value = settingsState.value.copy(backgroundCheckPeriod = event.period)
                    schedulePeriodicWork(event.period.period)
                }
            }

            is SettingsEvent.OnPermissionResult -> {
                if (!event.isGranted && !settingsState.value.visiblePermissionDialogQueue.contains(event.permission)) {
                    _settingsState.value = settingsState.value.copy(
                        visiblePermissionDialogQueue = settingsState.value.visiblePermissionDialogQueue + event.permission
                    )
                }
            }

            is SettingsEvent.EnableDarkTheme -> {
                viewModelScope.launch {
                    useCases.enableDarkTheme(event.enabled)
                    _settingsState.value = settingsState.value.copy(isDarkThemeEnabled = useCases.isDarkThemeEnabled().first())
                }
            }

            SettingsEvent.CancelWork -> {
                _settingsState.value = settingsState.value.copy(isWorkInProgress = true)
                workManager.cancelUniqueWork(Constants.WORKER_UNIQUE_PERIODIC_WORK_NAME)
            }

            SettingsEvent.SchedulePeriodicWork -> {
                _settingsState.value = settingsState.value.copy(isWorkInProgress = true)
                schedulePeriodicWork(backgroundCheckPeriodState.value.period)
            }

            SettingsEvent.DismissDialog -> {
                if (settingsState.value.visiblePermissionDialogQueue.isNotEmpty()) {
                    _settingsState.value = _settingsState.value.copy(
                        visiblePermissionDialogQueue = settingsState.value.visiblePermissionDialogQueue.drop(1)
                    )
                }
            }

            is SettingsEvent.CreateBackup -> createBackup(event.firestoreUserName, event.context)

            is SettingsEvent.UpdateBackup -> updateBackup(event.firestoreUser, event.context)

            is SettingsEvent.DeleteBackup -> deleteBackup(event.firestoreUser, event.context)
        }
    }

    /** WorkManager */

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

        _settingsState.value = settingsState.value.copy(isWorkInProgress = false)
    }

    /** Firestore database */

    private fun createBackup(firestoreUserName: String, context: Context) {
        viewModelScope.launch {
            val cases = useCases.getFavoriteCases().first()
            useCases.createBackup(firestoreUserName, cases).collect { result ->
                when (result) {
                    is FirestoreResult.Success -> showToast(result.data, context)
                    is FirestoreResult.Error -> showToast(result.exception.message, context)
                    else -> return@collect
                }
            }
        }
    }

    private fun updateBackup(firestoreUser: FirestoreUser, context: Context) {
        viewModelScope.launch {
            useCases.updateBackup(firestoreUser).collect { result ->
                when (result) {
                    is FirestoreResult.Success -> showToast(result.data, context)
                    is FirestoreResult.Error -> showToast(result.exception.message, context)
                    else -> return@collect
                }
            }
        }
    }

    private fun deleteBackup(firestoreUser: FirestoreUser, context: Context) {
        viewModelScope.launch {
            useCases.deleteBackup(firestoreUser).collect { result ->
                when (result) {
                    is FirestoreResult.Success -> showToast(result.data, context)
                    is FirestoreResult.Error -> showToast(result.exception.message, context)
                    else -> return@collect
                }
            }
        }
    }

    private fun showToast(message: String?, context: Context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
