package com.omtorney.snapcase.search.presentation

import com.omtorney.snapcase.common.domain.model.Case

data class SearchState(
    val cases: List<Case> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = ""
)
