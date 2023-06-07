package com.omtorney.snapcase.schedule.presentation

sealed class ScheduleEvent {
    data class SelectJudge(val query: String) : ScheduleEvent()
    data class FilterByParticipant(val query: String) : ScheduleEvent()
    object ToggleSearchSection : ScheduleEvent()
    object ResetJudge : ScheduleEvent()
    object ResetParticipant : ScheduleEvent()
}
