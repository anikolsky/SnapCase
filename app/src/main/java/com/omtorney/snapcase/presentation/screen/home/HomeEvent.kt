package com.omtorney.snapcase.presentation.screen.home

sealed class HomeEvent {
    data class SetSelectedCourt(val courtTitle: String) : HomeEvent()
}