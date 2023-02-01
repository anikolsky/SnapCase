package com.omtorney.snapcase.presentation.recent

import com.omtorney.snapcase.domain.model.Case

sealed class RecentEvent {
    data class Refresh(val cases: List<Case>) : RecentEvent()
    object Clear : RecentEvent()
}
