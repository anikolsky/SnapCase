package com.omtorney.snapcase.presentation.act

data class ActState(
    val text: ArrayList<String> = arrayListOf(),
    val isLoading: Boolean = false,
    val error: String = ""
)