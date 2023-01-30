package com.omtorney.snapcase.presentation.schedule

import com.omtorney.snapcase.domain.model.Case

data class ScheduleState(
    val cases: List<Case> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = ""
)
