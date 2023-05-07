package com.omtorney.snapcase.favorites.presentation

import com.omtorney.snapcase.common.domain.model.Case

sealed class FavoritesEvent {
    data class Delete(val case: Case) : FavoritesEvent()
    data class Refresh(val cases: List<Case>) : FavoritesEvent()
}
