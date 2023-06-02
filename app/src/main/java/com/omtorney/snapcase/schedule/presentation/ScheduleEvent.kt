package com.omtorney.snapcase.schedule.presentation

import com.omtorney.snapcase.common.domain.model.Case

sealed class ScheduleEvent {
    data class CacheCase(val case: Case) : ScheduleEvent()
    data class SelectJudge(val judge: String) : ScheduleEvent()
    object ResetJudge : ScheduleEvent()
}
