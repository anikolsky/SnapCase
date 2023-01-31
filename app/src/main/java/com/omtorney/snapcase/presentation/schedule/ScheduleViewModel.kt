package com.omtorney.snapcase.presentation.schedule

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omtorney.snapcase.domain.court.Courts
import com.omtorney.snapcase.domain.usecase.CaseUseCases
import com.omtorney.snapcase.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val caseUseCases: CaseUseCases
) : ViewModel() {

    private val _state = mutableStateOf(ScheduleState())
    val state: State<ScheduleState> = _state

    fun showSchedule(date: String) = viewModelScope.launch {
        caseUseCases.showSchedule(Courts.Dmitrov, date).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _state.value = ScheduleState(isLoading = true)
                }
                is Resource.Success -> {
                    _state.value = ScheduleState(cases = result.data ?: emptyList())
                }
                is Resource.Error -> {
                    _state.value = ScheduleState(error = result.message ?: "Unexpected error")
                }
            }
        } // .launchIn(viewModelScope)
    }
}