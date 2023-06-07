package com.omtorney.snapcase.detail.presentation

import com.omtorney.snapcase.common.domain.court.Court
import com.omtorney.snapcase.common.domain.model.Case

sealed class DetailEvent {
    data class Save(val case: Case) : DetailEvent()
    data class Load(val court: Court) : DetailEvent()
    data class Delete(val case: Case) : DetailEvent()
}
