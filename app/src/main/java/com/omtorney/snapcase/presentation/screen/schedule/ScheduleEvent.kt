package com.omtorney.snapcase.presentation.screen.schedule

sealed class ScheduleEvent {
    data class SelectJudge(val query: String) : ScheduleEvent()
    data class FilterByParticipant(val query: String) : ScheduleEvent()
    object ToggleSearchSection : ScheduleEvent()
    object ResetJudge : ScheduleEvent()
    object ResetParticipant : ScheduleEvent()
}
