package com.omtorney.snapcase.presentation.screen.detail

import com.omtorney.snapcase.domain.court.Court
import com.omtorney.snapcase.domain.model.Case

sealed class DetailEvent {
    data class Save(val case: Case) : DetailEvent()
    data class Load(val court: Court) : DetailEvent()
    data class Delete(val case: Case) : DetailEvent()
}
