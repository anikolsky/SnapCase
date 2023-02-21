package com.omtorney.snapcase.presentation.common

sealed class UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent()
    object Save : UiEvent()
    object Delete : UiEvent()
}