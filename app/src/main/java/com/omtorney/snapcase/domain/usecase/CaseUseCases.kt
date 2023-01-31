package com.omtorney.snapcase.domain.usecase

data class CaseUseCases(
    val checkCase: CheckCase,
    val deleteCase: DeleteCase,
    val fillCase: FillCase,
    val getFavoriteCases: GetFavoriteCases,
    val loadActText: LoadActText,
    val saveCase: SaveCase,
    val searchCase: SearchCase,
    val showSchedule: ShowSchedule,
    val updateCase: UpdateCase
)
