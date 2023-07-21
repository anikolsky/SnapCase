package com.omtorney.snapcase.firebase.presentation

import com.omtorney.snapcase.data.remote.database.FirestoreUser

data class FirestoreUserState(
    val data: List<FirestoreUser?>? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
