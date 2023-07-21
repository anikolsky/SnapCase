package com.omtorney.snapcase.presentation.screen.home

import com.omtorney.snapcase.network.NetworkState

data class HomeState(
    val selectedCourt: String = "",
    val networkState: NetworkState = NetworkState.Available
)
