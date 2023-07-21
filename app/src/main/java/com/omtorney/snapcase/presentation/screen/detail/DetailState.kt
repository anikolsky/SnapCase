package com.omtorney.snapcase.presentation.screen.detail

import com.omtorney.snapcase.domain.model.Case

data class DetailState(
    val case: Case = Case("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""),
    val isLoading: Boolean = false,
    val error: String = "",
    val isFavorite: Boolean = false
)
