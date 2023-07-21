package com.omtorney.snapcase.presentation.screen.schedule

import com.omtorney.snapcase.domain.model.Case

data class ScheduleState(
    val cases: List<Case> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = "",
    val isSearchSectionVisible: Boolean = false,
    val selectedJudge: String = "",
    val filteredCases: List<Case> = emptyList(),
    val participantQuery: String = ""
)
