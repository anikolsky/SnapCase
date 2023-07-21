package com.omtorney.snapcase.presentation.screen.recent

import com.omtorney.snapcase.domain.model.Case

data class RecentState(
    val cases: List<Case> = emptyList()
)
