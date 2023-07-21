package com.omtorney.snapcase.presentation.screen.favorites

import com.omtorney.snapcase.domain.model.Case

sealed class FavoritesEvent {
    data class Delete(val case: Case) : FavoritesEvent()
//    data class Refresh(val cases: List<Case>, val court: Court) : FavoritesEvent()
}
