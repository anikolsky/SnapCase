package com.omtorney.snapcase.network

sealed interface NetworkState {
    object Available : NetworkState
    object Unavailable : NetworkState
    object Lost : NetworkState
    object Losing : NetworkState
}
