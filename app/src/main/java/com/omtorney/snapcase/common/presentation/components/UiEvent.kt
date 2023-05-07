package com.omtorney.snapcase.common.presentation.components

sealed class UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent()
    object Save : UiEvent()
    object Delete : UiEvent()
}