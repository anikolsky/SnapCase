package com.omtorney.snapcase.recent.presentation

import com.omtorney.snapcase.common.domain.model.Case

data class RecentState(
    val cases: List<Case> = emptyList()
)
