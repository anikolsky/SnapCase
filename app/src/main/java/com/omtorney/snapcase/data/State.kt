package com.omtorney.snapcase.data

sealed class State {
    object Default : State()
    object Success : State()
    data class Failure(val message: String) : State()
}
