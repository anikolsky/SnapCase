package com.omtorney.snapcase.presentation.screen.act

data class ActState(
    val text: String = "",
    val url: String = "",
    val isLoading: Boolean = false,
    val error: String = ""
)