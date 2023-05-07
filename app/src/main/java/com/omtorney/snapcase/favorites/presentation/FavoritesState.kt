package com.omtorney.snapcase.favorites.presentation

import com.omtorney.snapcase.common.domain.model.Case

data class FavoritesState(
    val cases: List<Case> = emptyList()
)
