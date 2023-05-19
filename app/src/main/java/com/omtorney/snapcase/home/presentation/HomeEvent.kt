package com.omtorney.snapcase.home.presentation

sealed class HomeEvent {
    data class SetSelectedCourt(val courtTitle: String) : HomeEvent()
}