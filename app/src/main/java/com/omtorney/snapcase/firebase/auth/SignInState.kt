package com.omtorney.snapcase.firebase.auth

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInErrorMessage: String? = null
)
