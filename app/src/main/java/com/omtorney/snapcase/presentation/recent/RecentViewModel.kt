package com.omtorney.snapcase.presentation.recent

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omtorney.snapcase.data.database.CaseDao
import com.omtorney.snapcase.domain.court.Courts
import com.omtorney.snapcase.domain.model.Case
import com.omtorney.snapcase.domain.usecase.CaseUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecentViewModel @Inject constructor(
    private val caseUseCases: CaseUseCases
) : ViewModel() {

    private val _state = mutableStateOf(RecentState())
    val state: State<RecentState> = _state

    private var getRecentCasesJob: Job? = null

    init {
        getRecentCases()
    }

    private fun getRecentCases() {
        getRecentCasesJob?.cancel()
        getRecentCasesJob = caseUseCases.getRecentCases().onEach { cases ->
            _state.value = state.value.copy(cases = cases)
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: RecentEvent) {
        when (event) {
            is RecentEvent.Clear -> {
                viewModelScope.launch {
                    caseUseCases.clearRecentCases()
                }
            }
            is RecentEvent.Refresh -> { }
        }
    }
}