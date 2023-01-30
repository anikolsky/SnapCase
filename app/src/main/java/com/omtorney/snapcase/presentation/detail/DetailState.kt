package com.omtorney.snapcase.presentation.detail

import com.omtorney.snapcase.domain.model.Case

data class DetailState(
    val case: Case? = null,
    val isLoading: Boolean = false,
    val error: String = ""
)