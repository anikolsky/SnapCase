package com.omtorney.snapcase.act.presentation

data class ActState(
    val text: String = "",
    val url: String = "",
    val isLoading: Boolean = false,
    val error: String = ""
)