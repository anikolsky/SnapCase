package com.omtorney.snapcase.schedule.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omtorney.snapcase.common.domain.usecase.UseCases
import com.omtorney.snapcase.common.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val useCases: UseCases,
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
        val date = savedStateHandle.get<String>("scheduleDate")!!
        val courtTitle = savedStateHandle.get<String>("courtTitle")!!
        showSchedule(courtTitle, date)
    }

    fun onEvent(event: ScheduleEvent) {
        when (event) {
            is ScheduleEvent.SelectJudge -> {
                _state.value = state.value.copy(selectedJudge = event.query)
                updateFilteredCases(_state.value.selectedJudge, "judge") // TODO make enum
            }

            is ScheduleEvent.FilterByParticipant -> {
                _state.value = state.value.copy(participantQuery = event.query)
                updateFilteredCases(_state.value.participantQuery, "participant") // TODO make enum
            }

            ScheduleEvent.ToggleSearchSection -> {
                _state.value = state.value.copy(isSearchSectionVisible = !state.value.isSearchSectionVisible)
            }

            ScheduleEvent.ResetJudge -> {
                _state.value = state.value.copy(selectedJudge = "")
                updateFilteredCases(_state.value.selectedJudge, "judge") // TODO make enum
            }

            ScheduleEvent.ResetParticipant -> {
                _state.value = state.value.copy(participantQuery = "")
                updateFilteredCases(_state.value.participantQuery, "participant") // TODO make enum
            }
        }
    }

    private fun showSchedule(courtTitle: String, date: String) = viewModelScope.launch {
        useCases.showSchedule(courtTitle, date).collect { result ->
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

    private fun updateFilteredCases(query: String, type: String) {
        val currentCases = _state.value.cases
        val filteredCases = if (query.isBlank()) {
            currentCases
        } else {
            currentCases.filter { it.doesFieldMatchQuery(query.lowercase(), type) }
        }
        _state.value = state.value.copy(filteredCases = filteredCases)
    }
}
