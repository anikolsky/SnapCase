package com.omtorney.snapcase.presentation.detail

import com.omtorney.snapcase.domain.model.Case

sealed class DetailEvent {
    data class Save(val case: Case) : DetailEvent()
    data class Delete(val case: Case) : DetailEvent()
    object Fill : DetailEvent()
}
