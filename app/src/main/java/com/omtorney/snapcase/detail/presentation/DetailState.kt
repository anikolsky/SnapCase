package com.omtorney.snapcase.detail.presentation

import com.omtorney.snapcase.common.domain.model.Case

data class DetailState(
    val case: Case? = null,
    val isLoading: Boolean = false,
    val error: String = ""
)