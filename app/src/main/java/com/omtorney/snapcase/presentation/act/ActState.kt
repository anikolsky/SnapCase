package com.omtorney.snapcase.presentation.act

data class ActState(
    val text: String = "",
    val isLoading: Boolean = false,
    val error: String = ""
)