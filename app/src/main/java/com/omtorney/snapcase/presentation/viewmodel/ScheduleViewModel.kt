package com.omtorney.snapcase.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omtorney.snapcase.data.court.Courts
import com.omtorney.snapcase.data.model.Case
import com.omtorney.snapcase.domain.usecase.ShowScheduleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val showScheduleUseCase: ShowScheduleUseCase
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _schedule = MutableStateFlow(listOf<Case>())
    val schedule = _schedule.asStateFlow()

    fun showSchedule(date: String) = viewModelScope.launch {
        _isLoading.value = true
        _schedule.value = showScheduleUseCase.execute(Courts.Dmitrov, date)
        _isLoading.value = false
    }
}