package com.omtorney.snapcase.presentation.schedule

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omtorney.snapcase.domain.court.Courts
import com.omtorney.snapcase.domain.usecase.CaseUseCases
import com.omtorney.snapcase.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val caseUseCases: CaseUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(ScheduleState())
    val state: State<ScheduleState> = _state

    init {
        savedStateHandle.get<String>("scheduleDate")?.let { date ->
            showSchedule(date)
//            Log.d("TESTLOG", "ScheduleViewModel: init date: $date")
        }
    }

    private fun showSchedule(date: String) = viewModelScope.launch {
        caseUseCases.showSchedule(Courts.Dmitrov, date).collect { result ->
            when (result) {
                is Resource.Loading -> {
                    _state.value = ScheduleState(isLoading = true)
//                    Log.d("TESTLOG", "ScheduleViewModel: Resource.Loading...")
                }
                is Resource.Success -> {
                    _state.value = ScheduleState(cases = result.data ?: emptyList())
//                    Log.d("TESTLOG", "ScheduleViewModel: Resource.Success: ${result.data}")
                }
                is Resource.Error -> {
                    _state.value = ScheduleState(error = result.message ?: "Unexpected error")
//                    Log.d("TESTLOG", "ScheduleViewModel: Resource.Error: ${result.message}")
                }
            }
        }
    }
}