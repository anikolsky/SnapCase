package com.omtorney.snapcase.recent.presentation

import com.omtorney.snapcase.common.domain.model.Case

sealed class RecentEvent {
    data class Refresh(val cases: List<Case>) : RecentEvent()
    object Clear : RecentEvent()
}
