package com.omtorney.snapcase.home.presentation

import com.omtorney.snapcase.network.NetworkState

data class HomeState(
    val selectedCourt: String = "",
    val networkState: NetworkState = NetworkState.Available
)
