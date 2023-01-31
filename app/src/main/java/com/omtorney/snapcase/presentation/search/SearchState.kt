package com.omtorney.snapcase.presentation.search

import com.omtorney.snapcase.domain.model.Case

data class SearchState(
    val cases: List<Case> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = ""
)
