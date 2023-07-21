package com.omtorney.snapcase.presentation.screen.favorites

import com.omtorney.snapcase.domain.model.Case

data class FavoritesState(
    val cases: List<Case> = emptyList()
)
