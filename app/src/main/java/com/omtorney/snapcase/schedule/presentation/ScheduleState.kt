package com.omtorney.snapcase.schedule.presentation

import com.omtorney.snapcase.common.domain.model.Case

data class ScheduleState(
    val cases: List<Case> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = "",
    val isSearchSectionVisible: Boolean = false,
    val selectedJudge: String = "",
    val filteredCases: List<Case> = emptyList(),
    val participantQuery: String = ""
)
