package com.omtorney.snapcase.presentation.schedule

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omtorney.snapcase.domain.court.Court
import com.omtorney.snapcase.domain.court.Courts
import com.omtorney.snapcase.domain.model.Case
import com.omtorney.snapcase.domain.usecase.CaseUseCases
import com.omtorney.snapcase.util.Resource
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

    private val _selectedJudge = MutableStateFlow("")
    val selectedJudge = _selectedJudge.asStateFlow()

    private val _filteredCases = MutableStateFlow(state.value.cases)
    val filteredCases = selectedJudge
        .combine(_filteredCases) { text, cases ->
            if (text.isBlank()) {
                cases
            } else {
                cases.filter { it.doesJudgeMatchSearchQuery(text) }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = _filteredCases.value
        )

    init {
        val date =  savedStateHandle.get<String>("scheduleDate")!!
        val courtTitle = savedStateHandle.get<String>("courtTitle")!!
        showSchedule(courtTitle, date)
    }

    fun cacheCase(case: Case) {
        viewModelScope.launch {
            caseUseCases.saveCase(case)
        }
    }

    fun onJudgeSelect(text: String) {
        _selectedJudge.value = text
    }

    private fun showSchedule(courtTitle: String, date: String) = viewModelScope.launch {
        caseUseCases.showSchedule(courtTitle, date).collect { result ->
            when (result) {
                is Resource.Loading -> {
                    _state.value = ScheduleState(isLoading = true)
                }
                is Resource.Success -> {
                    val cases = result.data ?: emptyList()
                    _state.value = ScheduleState(cases = cases)
                    _filteredCases.value = cases
                }
                is Resource.Error -> {
                    _state.value = ScheduleState(error = result.message ?: "Unexpected error")
                }
            }
        }
    }
}