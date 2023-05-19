package com.omtorney.snapcase.search.presentation

import com.omtorney.snapcase.common.domain.model.Case

sealed class SearchEvent {
    data class CacheCase(val case: Case) : SearchEvent()
}
