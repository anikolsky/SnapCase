package com.omtorney.snapcase.schedule.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omtorney.snapcase.common.domain.model.Case
import com.omtorney.snapcase.common.domain.usecase.CaseUseCases
import com.omtorney.snapcase.common.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val caseUseCases: CaseUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(ScheduleState())
    val state: State<ScheduleState> = _state

//    private val _filteredCases = MutableStateFlow(state.value.cases)
//    val filteredCases = state.value.selectedJudge // selectedJudge was StateFlow and now it is String
//        .combine(_filteredCases) { text, cases ->
//            if (text.isBlank()) {
//                cases
//            } else {
//                cases.filter { it.doesJudgeMatchSearchQuery(text) }
//            }
//        }
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(5000),
//            initialValue = _filteredCases.value
//        )

    init {
        val date =  savedStateHandle.get<String>("scheduleDate")!!
        val courtTitle = savedStateHandle.get<String>("courtTitle")!!
        showSchedule(courtTitle, date)
    }

    fun onEvent(event: ScheduleEvent) {
        when (event) {
            is ScheduleEvent.CacheCase -> {
                viewModelScope.launch {
                    caseUseCases.saveCase(event.case)
                }
            }
            is ScheduleEvent.SelectJudge -> {
                _state.value = state.value.copy(selectedJudge = event.judge)
                updateFilteredCases()
            }
        }
    }

    private fun showSchedule(courtTitle: String, date: String) = viewModelScope.launch {
        caseUseCases.showSchedule(courtTitle, date).collect { result ->
            when (result) {
                is Resource.Loading -> {
                    _state.value = state.value.copy(
                        isLoading = true
                    )
                }
                is Resource.Success -> {
                    val cases = result.data ?: emptyList()
                    _state.value = state.value.copy(
                        cases = cases,
                        filteredCases = cases,
                        isLoading = false
                    )
                }
                is Resource.Error -> {
                    _state.value = state.value.copy(
                        error = result.message ?: "Unexpected error",
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun updateFilteredCases() {
        val currentCases = _state.value.cases
        val selectedJudge = _state.value.selectedJudge
        val filteredCases = if (selectedJudge.isBlank()) {
            currentCases
        } else {
            currentCases.filter { it.doesJudgeMatchSearchQuery(selectedJudge) }
        }
        _state.value = state.value.copy(filteredCases = filteredCases)
    }
}
